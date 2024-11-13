package ru.yandex.practicum.filmorate.storage.impl;

import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public InMemoryUserStorage(User... inputUsers) {
        for (User user : inputUsers) {
            addUser(user);
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
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

    @Override
    public User findUser(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> findUsers(Collection<Long> ids) {
        return ids.stream()
                .map(this::findUser)
                .toList();
    }

    private long getNextId() {
        long maxId = users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++maxId;
    }
}
