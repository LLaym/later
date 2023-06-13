package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final Mapper mapper;

    @GetMapping
    public List<ItemDTO> get(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getItems(userId).stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @PostMapping
    public ItemDTO create(@RequestHeader("X-Later-User-Id") Long userId,
                          @RequestBody ItemCreationDTO itemDTO) {
        Item item = new Item();
        item.setUserId(itemDTO.getUserId());
        item.setUrl(itemDTO.getUrl());
        item.setTags(itemDTO.getTags());

        Item itemCreated = itemService.addNewItem(userId, item);

        return mapper.toDto(itemCreated);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Later-User-Id") long userId,
                       @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}