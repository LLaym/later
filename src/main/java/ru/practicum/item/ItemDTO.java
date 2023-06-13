package ru.practicum.item;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ItemDTO {
    private String url;
    private Set<String> tags = new HashSet<>();
}
