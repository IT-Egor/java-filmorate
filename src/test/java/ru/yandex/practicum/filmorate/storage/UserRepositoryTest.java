package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
class UserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    void getAllUsers() {
        List<User> expected = List.of(
                new User(1, "user1@example.com", "user1", "Иван Иванов", LocalDate.of(1990, 1, 1)),
                new User(2, "user2@example.com", "user2", "Мария Петрова", LocalDate.of(1985, 6, 15)),
                new User(3, "user3@example.com", "user3", "Сергей Сидоров", LocalDate.of(1970, 3, 20))
        );
        assertArrayEquals(expected.toArray(), userRepository.getAllUsers().toArray());
    }

    @Test
    void findUser() {
        User user = new User(1, "user1@example.com", "user1", "Иван Иванов", LocalDate.of(1990, 1, 1));
        assertEquals(user, userRepository.findUser(1).get());
    }

    @Test
    void addUser() {
        User user = new User(1, "user4@example.com", "user4", "name4", LocalDate.now());
        user.setId(userRepository.addUser(user));
        assertEquals(user, userRepository.findUser(user.getId()).get());
    }

    @Test
    void updateUser() {
        User user = new User(1, "user@gmail.com", "login", "name", LocalDate.now());
        User updatedUser = new User(1, "updated@gmail.com", "login", "name", LocalDate.now());
        user.setId(userRepository.addUser(user));
        updatedUser.setId(user.getId());
        userRepository.updateUser(updatedUser);
        assertEquals(updatedUser, userRepository.findUser(updatedUser.getId()).get());
    }
}