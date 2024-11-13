package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

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

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.makeFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        User user = userStorage.findUser(id);
        return userStorage.findUsers(user.getFriends());
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.commonFriends(id, otherId);
    }
}
