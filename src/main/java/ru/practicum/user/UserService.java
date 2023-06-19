package ru.practicum.user;

import java.util.List;

interface UserService {
    User addNew(User user);

    List<User> getAll();
}