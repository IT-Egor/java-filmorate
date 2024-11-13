package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;
import java.time.LocalDate;

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
        userService.makeFriends(user, user2);
        assertTrue(user.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user.getId()));
    }

    @Test
    void shouldNotMakeFriendsIfFriendNotFound() {
        User user2 = new User(0, "email4@gmail.com", "login4", "name4", LocalDate.now());
        assertThrows(NotFoundException.class, () -> userService.makeFriends(user, user2));
    }

    @Test
    void shouldRemoveFriend() {
        userService.makeFriends(user, user2);
        userService.removeFriends(user, user2);
        assertFalse(user2.getFriends().contains(user.getId()));
        assertFalse(user.getFriends().contains(user2.getId()));
    }

    @Test
    void shouldDoNothingIfFriendNotFoundWhileRemoving() {
        userService.removeFriends(user, user2);
        assertFalse(user2.getFriends().contains(user.getId()));
        assertFalse(user.getFriends().contains(user2.getId()));
    }

    @Test
    void shouldReturnCommonFriends() {
        userService.makeFriends(user, user2);
        userService.makeFriends(user, user3);
        userService.makeFriends(user2, user3);
        userService.makeFriends(user2, user4);
        assertArrayEquals(new Long[]{user3.getId()}, userService.commonFriends(user, user2).toArray());
        assertArrayEquals(new Long[]{user2.getId()}, userService.commonFriends(user, user3).toArray());
        assertArrayEquals(new Long[]{user2.getId()}, userService.commonFriends(user, user4).toArray());
    }
}