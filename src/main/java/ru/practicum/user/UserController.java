package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final Mapper mapper;

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers().stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @PostMapping
    public UserDTO create(@RequestBody UserCreationDTO userDTO) {
        User user = mapper.toUser(userDTO);
        User userSaved = userService.saveUser(user);

        return mapper.toDto(userSaved);
    }
}