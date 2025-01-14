package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MergeUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final EventService eventService;

    @GetMapping
    public Collection<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody MergeUserRequest userMerge) {
        return userService.saveUser(userMerge);
    }

    @PutMapping
    public UserDTO updateUser(@Valid @RequestBody MergeUserRequest userMerge) {
        return userService.updateUser(userMerge);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.makeFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriends(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<UserDTO> getFriends(@PathVariable Long userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<UserDTO> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.commonFriends(id, otherId);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable Long userId) {
        userService.removeUser(userId);
    }

    @GetMapping("{id}/feed")
    public Collection<Event> getFeed(@PathVariable Long userId) { return eventService.getFeed(userId); }
}


