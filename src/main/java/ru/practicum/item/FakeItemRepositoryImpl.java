package ru.practicum.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FakeItemRepositoryImpl implements ItemRepository {
    private static final Map<Long, Item> FAKE_ITEMS = new HashMap<>();
    private static Long nextId = 1L;

    @Override
    public List<Item> findByUserId(long userId) {
        List<Item> items = new ArrayList<>();

        for (Item item : FAKE_ITEMS.values()) {
            if (item.getUserId() == userId) {
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public Item save(Item item) {
        item.setId(nextId++);
        FAKE_ITEMS.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        FAKE_ITEMS.remove(itemId);
    }
}
