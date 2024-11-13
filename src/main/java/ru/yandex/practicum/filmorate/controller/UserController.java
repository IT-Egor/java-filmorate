package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserStorage userStorage;

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }
}
