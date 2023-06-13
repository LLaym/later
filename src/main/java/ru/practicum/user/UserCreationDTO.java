package ru.practicum.user;

import lombok.Data;

@Data
public class UserCreationDTO {
    private String firstName;
    private String lastName;
    private String email;
}
