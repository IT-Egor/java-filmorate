package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    public final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void makeFriends(Long userId, Long friendId) {
        User user = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriends(Long userId, Long friendId) {
        User user = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Collection<User> commonFriends(Long userId1, Long userId2) {
        User user1 = userStorage.findUser(userId1);
        User user2 = userStorage.findUser(userId2);
        Set<Long> commonFriends = new HashSet<>(user1.getFriends());
        commonFriends.retainAll(user2.getFriends());
        return userStorage.findUsers(commonFriends);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User saveUser(User user) {
        Validator.validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        Validator.validateUser(user);
        return userStorage.updateUser(user);
    }

    public User findUser(Long id) {

        return userStorage.findUser(id);
    }

    public Collection<User> findUsers(Collection<Long> ids) {
        return userStorage.findUsers(ids);
    }
}
