package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public List<Item> getAllByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    @Override
    public Item addNew(Item item) {
        return repository.save(item);
    }

    @Transactional
    @Override
    public void deleteById(long userId, long itemId) {
        repository.deleteByUserIdAndId(userId, itemId);
    }
}