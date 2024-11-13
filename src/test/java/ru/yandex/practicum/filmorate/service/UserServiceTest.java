package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserStorage userStorage;

    private User user = new User(0, "email@gmail.com", "login", "name", LocalDate.now());
    private User user2 = new User(0, "email2@gmail.com", "login2", "name2", LocalDate.now());
    private User user3 = new User(0, "email3@gmail.com", "login3", "name3", LocalDate.now());
    private User user4 = new User(0, "email4@gmail.com", "login4", "name4", LocalDate.now());

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage(
                user, user2, user3, user4
        );
        userService = new UserService(userStorage);
    }

    @Test
    void shouldMakeFriends() {
        userService.makeFriends(user.getId(), user2.getId());
        assertTrue(user.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user.getId()));
    }

    @Test
    void shouldNotMakeFriendsIfFriendNotFound() {
        User user2 = new User(0, "email4@gmail.com", "login4", "name4", LocalDate.now());
        assertThrows(NotFoundException.class, () ->
                userService.makeFriends(user.getId(), user2.getId()));
    }

    @Test
    void shouldRemoveFriend() {
        userService.makeFriends(user.getId(), user2.getId());
        userService.removeFriends(user.getId(), user2.getId());
        assertFalse(user2.getFriends().contains(user.getId()));
        assertFalse(user.getFriends().contains(user2.getId()));
    }

    @Test
    void shouldDoNothingIfFriendNotFoundWhileRemoving() {
        userService.removeFriends(user.getId(), user2.getId());
        assertFalse(user2.getFriends().contains(user.getId()));
        assertFalse(user.getFriends().contains(user2.getId()));
    }

    @Test
    void shouldNotRemoveFriendIfFriendNotFoundWhileRemoving() {
        User user2 = new User(0, "email4@gmail.com", "login4", "name4", LocalDate.now());
        assertThrows(NotFoundException.class, () ->
                userService.removeFriends(user.getId(), user2.getId()));
        assertThrows(NotFoundException.class, () ->
                userService.removeFriends(user2.getId(), user.getId()));
    }

    @Test
    void shouldReturnCommonFriends() {
        userService.makeFriends(user.getId(), user2.getId());
        userService.makeFriends(user.getId(), user3.getId());
        userService.makeFriends(user2.getId(), user3.getId());
        userService.makeFriends(user2.getId(), user4.getId());
        assertArrayEquals(new User[]{user3}, userService.commonFriends(user.getId(), user2.getId()).toArray());
        assertArrayEquals(new User[]{user2}, userService.commonFriends(user.getId(), user3.getId()).toArray());
        assertArrayEquals(new User[]{user2}, userService.commonFriends(user.getId(), user4.getId()).toArray());
    }
}