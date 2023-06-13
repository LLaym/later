package ru.practicum.item;

import lombok.Data;

import java.util.Set;

@Data
public class ItemCreationDTO {
    private Long userId;
    private String url;
    private Set<String> tags;
}
