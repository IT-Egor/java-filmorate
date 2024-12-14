package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MergeUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    public final UserStorage userStorage;

    public void makeFriends(Long userId, Long friendId) {
//        User user = findUser(userId);
//        User friend = findUser(friendId);
//        user.addFriend(friendId);
//        friend.addFriend(userId);
        throw new RuntimeException("Not implemented");
    }

    public void removeFriends(Long userId, Long friendId) {
//        User user = findUser(userId);
//        User friend = findUser(friendId);
//        user.removeFriend(friendId);
//        friend.removeFriend(userId);
        throw new RuntimeException("Not implemented");
    }

    public Collection<User> commonFriends(Long userId1, Long userId2) {
//        User user1 = findUser(userId1);
//        User user2 = findUser(userId2);
//        Set<Long> commonFriends = new HashSet<>(user1.getFriends());
//        commonFriends.retainAll(user2.getFriends());
//        return findUsers(commonFriends);
        throw new RuntimeException("Not implemented");
    }

    public Collection<UserDTO> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::mapUserToUserDTO).toList();
    }

    public UserDTO saveUser(MergeUserRequest userMerge) {
        User user = UserMapper.mapMergeRequestToUser(userMerge);
        Validator.validateUser(user);
        return findUser(userStorage.addUser(user));
    }

    public UserDTO updateUser(MergeUserRequest userMerge) {
        User user = UserMapper.mapMergeRequestToUser(userMerge);
        Validator.validateUser(user);
        if (userStorage.updateUser(user) == 0) {
            throw new NotFoundException(String.format("User with id=%s not found", user.getId()));
        }
        return findUser(user.getId());
    }

    public UserDTO findUser(Long id) {
        Optional<User> userOpt = userStorage.findUser(id);
        if (userOpt.isPresent()) {
            return UserMapper.mapUserToUserDTO(userOpt.get());
        } else {
            throw new NotFoundException(String.format("User with id=%s not found", id));
        }
    }
}
