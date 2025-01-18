package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MergeUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendService friendService;
    private final EventService eventService;

    public void makeFriends(Long userId, Long friendId) {
        findUser(userId);
        findUser(friendId);
        friendService.addFriend(userId, friendId);
        eventService.createEvent(userId, "FRIEND", "ADD", friendId);
    }

    public void removeFriends(Long userId, Long friendId) {
        findUser(userId);
        findUser(friendId);
        friendService.removeFriend(userId, friendId);
        eventService.createEvent(userId, "FRIEND", "REMOVE", friendId);
    }

    public Collection<UserDTO> commonFriends(Long userId1, Long userId2) {
        findUser(userId1);
        findUser(userId2);
        List<Long> user1Friends = new ArrayList<>(friendService.getUserFriends(userId1));
        List<Long> user2Friends = new ArrayList<>(friendService.getUserFriends(userId2));
        return user1Friends.stream().filter(user2Friends::contains).map(this::findUser).toList();
    }

    public Collection<UserDTO> getUserFriends(Long userId) {
        findUser(userId);
        Collection<Long> userFriends = friendService.getUserFriends(userId);
        return userFriends.stream().map(this::findUser).toList();
    }

    public Collection<UserDTO> getAllUsers() {
        return userRepository.getAllUsers().stream().map(UserMapper::mapUserToUserDTO).toList();
    }

    public UserDTO saveUser(MergeUserRequest userMerge) {
        User user = UserMapper.mapMergeRequestToUser(userMerge);
        return findUser(userRepository.addUser(user));
    }

    public UserDTO updateUser(MergeUserRequest userMerge) {
        User user = UserMapper.mapMergeRequestToUser(userMerge);
        if (userRepository.updateUser(user) == 0) {
            throw new NotFoundException(String.format("User with id=%s not found", user.getId()));
        }
        return findUser(user.getId());
    }

    public UserDTO findUser(Long id) {
        Optional<User> userOpt = userRepository.findUser(id);
        if (userOpt.isPresent()) {
            return UserMapper.mapUserToUserDTO(userOpt.get());
        } else {
            throw new NotFoundException(String.format("User with id=%s not found", id));
        }
    }

    public void removeUser(Long userId) {
        findUser(userId);
        userRepository.removeUser(userId);
    }
}
