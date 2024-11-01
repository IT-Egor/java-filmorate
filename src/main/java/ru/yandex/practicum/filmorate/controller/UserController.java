package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User requestUser) {
        User user = Validator.validateUser(requestUser);
        long id = getNextId();
        user.setId(id);
        users.put(id, user);
        log.info("User with id {} created", id);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User requestUser) {
        User user = Validator.validateUser(requestUser);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("User with id %d not found", user.getId()));
        }
        users.put(user.getId(), user);
        log.info("User with id {} updated", user.getId());
        return user;
    }


    private long getNextId() {
        long maxId = users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++maxId;
    }
}
