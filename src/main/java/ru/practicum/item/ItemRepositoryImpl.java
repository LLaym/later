package ru.practicum.item;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final Map<Long, List<Item>> FAKE_ITEMS = new HashMap<>();
    private static Long nextId = 1L;

    @Override
    public List<Item> findByUserId(long userId) {
        return FAKE_ITEMS.get(userId);
    }

    @Override
    public Item save(Item item) {
        item.setId(nextId++);
        FAKE_ITEMS.get(item.getUserId()).add(item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        FAKE_ITEMS.get(userId).removeIf(i -> i.getId().equals(itemId));
    }
}
