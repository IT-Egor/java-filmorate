package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest {

    private UserStorage userStorage;
    private User user;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        user = new User(0, "email@gmail.com", "login", "name", LocalDate.now());
    }

    @Test
    void shouldAddUser() {
        userStorage.addUser(user);
        assertEquals(userStorage.getUsers().toString(), List.of(user).toString());
    }

    @Test
    void shouldGenerateCorrectId() {
        userStorage.addUser(user);
        userStorage.addUser(new User(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                LocalDate.now()));
        userStorage.addUser(new User(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                LocalDate.now()));

        List<Long> ids = userStorage.getUsers().stream().map(User::getId).toList();
        assertEquals(ids.get(0), ids.get(1) - 1);
        assertEquals(ids.get(1), ids.get(2) - 1);
    }

    @Test
    void shouldUpdateUser() {
        userStorage.addUser(user);
        User updatedUser = new User(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                LocalDate.now());
        updatedUser.setName("updatedUser");

        userStorage.updateUser(updatedUser);
        assertEquals(userStorage.getUsers().toString(), List.of(updatedUser).toString());
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        assertEquals(0, userStorage.getUsers().size());
    }

    @Test
    void shouldNotUpdateUnexistedFilm() {
        userStorage.addUser(user);
        User updatedUser = new User(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                LocalDate.now());
        updatedUser.setId(0);

        assertThrows(NotFoundException.class, () -> userStorage.updateUser(updatedUser));
    }
}