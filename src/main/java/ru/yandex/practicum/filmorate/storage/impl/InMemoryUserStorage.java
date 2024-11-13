package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        Validator.validateUser(user);
        long id = getNextId();
        user.setId(id);
        users.put(id, user);
        log.info("User with id {} created", id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Validator.validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.info("User with id {} not found", user.getId());
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
