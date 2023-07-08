package ru.practicum.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.InsufficientPermissionException;
import ru.practicum.common.NotFoundException;
import ru.practicum.item.dto.AddItemRequest;
import ru.practicum.item.dto.GetItemRequest;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.QItem;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final UrlMetaDataRetriever urlMetaDataRetriever;

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> userItems = repository.findByUserId(userId);
        return ItemMapper.mapToItemDto(userItems);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(Long userId, AddItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InsufficientPermissionException("You do not have permission to perform this operation"));

        UrlMetaDataRetriever.UrlMetadata result = urlMetaDataRetriever.retrieve(request.getUrl());

        Optional<Item> maybeExistingItem = repository.findByUserAndResolvedUrl(user, result.getResolvedUrl());
        Item item;
        if (maybeExistingItem.isEmpty()) {
            item = repository.save(ItemMapper.mapToItem(result, user, request.getTags()));
        } else {
            item = maybeExistingItem.get();
            if (request.getTags() != null && !request.getTags().isEmpty()) {
                item.getTags().addAll(request.getTags());
                repository.save(item);
            }
        }
        return ItemMapper.mapToItemDto(item);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItems(GetItemRequest req) {
        QItem item = QItem.item;

        List<BooleanExpression> conditions = new ArrayList<>();

        conditions.add(item.user.id.eq(req.getUserId()));

        GetItemRequest.State state = req.getState();

        if (!state.equals(GetItemRequest.State.ALL)) {
            conditions.add(makeStateCondition(state));
        }

        GetItemRequest.ContentType contentType = req.getContentType();
        if (!contentType.equals(GetItemRequest.ContentType.ALL)) {
            conditions.add(makeContentTypeCondition(contentType));
        }

        if (req.hasTags()) {
            conditions.add(item.tags.any().in(req.getTags()));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Sort sort = makeOrderByClause(req.getSort());
        PageRequest pageRequest = PageRequest.of(0, req.getLimit(), sort);

        Iterable<Item> items = repository.findAll(finalCondition, pageRequest);
        return ItemMapper.mapToItemDto(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getUserItems(String lastName) {
        List<Item> foundItems = repository.findItemsByLastNamePrefix(lastName);
        return ItemMapper.mapToItemDto(foundItems);
    }

    @Override
    public ItemDto changeItem(long userId, ModifyItemRequest request) {
        Optional<Item> maybeItem = getAndCheckPermissions(userId, request.getItemId());
        if (maybeItem.isPresent()) {
            Item item = maybeItem.get();

            item.setUnread(!request.isRead());

            if (request.isReplaceTags()) {
                item.getTags().clear();
            }
            if (request.hasTags()) {
                item.getTags().addAll(request.getTags());
            }
            item = repository.save(item);
            return ItemMapper.mapToItemDto(item);
        } else {
            throw new NotFoundException("The item with id " + request.getItemId() + " was not found");
        }
    }

    private Optional<Item> getAndCheckPermissions(long userId, long itemId) {
        Optional<Item> maybeItem = repository.findById(itemId);
        if (maybeItem.isPresent()) {
            Item item = maybeItem.get();
            if (!item.getUser().getId().equals(userId)) {
                throw new InsufficientPermissionException("You do not have permission to perform this operation");
            }
        }
        return maybeItem;
    }

    private BooleanExpression makeStateCondition(GetItemRequest.State state) {
        if (state.equals(GetItemRequest.State.READ)) {
            return QItem.item.unread.isFalse();
        } else {
            return QItem.item.unread.isTrue();
        }
    }

    private BooleanExpression makeContentTypeCondition(GetItemRequest.ContentType contentType) {
        if (contentType.equals(GetItemRequest.ContentType.ARTICLE)) {
            return QItem.item.mimeType.eq("text");
        } else if (contentType.equals(GetItemRequest.ContentType.IMAGE)) {
            return QItem.item.mimeType.eq("image");
        } else {
            return QItem.item.mimeType.eq("video");
        }
    }

    private Sort makeOrderByClause(GetItemRequest.Sort sort) {
        switch (sort) {
            case TITLE:
                return Sort.by("title").ascending();
            case SITE:
                return Sort.by("resolvedUrl").ascending();
            case OLDEST:
                return Sort.by("dateResolved").ascending();
            case NEWEST:
            default:
                return Sort.by("dateResolved").descending();
        }
    }
}
