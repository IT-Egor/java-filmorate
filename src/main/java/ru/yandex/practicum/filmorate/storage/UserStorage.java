package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    Optional<User> findUser(long id);

    Collection<Optional<User>> findUsers(Collection<Long> ids);
}
