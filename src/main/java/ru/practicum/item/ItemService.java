package ru.practicum.item;

import java.util.List;

interface ItemService {
    List<Item> getAllByUserId(long userId);

    Item addNew(Item item);

    void deleteById(long userId, long itemId);
}