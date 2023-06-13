package ru.practicum.item;

public class Mapper {
    ItemDTO toDto(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setUrl(item.getUrl());
        itemDTO.setTags(item.getTags());

        return itemDTO;
    }

    Item toItem(ItemCreationDTO itemDTO) {
        Item item = new Item();
        item.setUserId(itemDTO.getUserId());
        item.setUrl(itemDTO.getUrl());
        item.setTags(itemDTO.getTags());

        return item;
    }
}
