package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody final User user) {
        int id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }


    private int getNextId() {
        int maxId = users.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
        return ++maxId;
    }
}
