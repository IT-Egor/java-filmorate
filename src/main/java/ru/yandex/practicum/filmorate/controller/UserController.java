package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @GetMapping
    public Collection<User> getUsers() {
        throw new RuntimeException("Not implemented");
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        throw new RuntimeException("Not implemented");
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        throw new RuntimeException("Not implemented");
    }
}
