package ru.practicum.item;

import java.util.List;

interface ItemService {
    List<Item> getItems(long userId);

    Item addNewItem(long userId, Item item);

    void deleteItem(long userId, long itemId);
}