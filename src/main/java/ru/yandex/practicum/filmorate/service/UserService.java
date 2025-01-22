package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MergeUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
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
        eventService.createEvent(userId, EventType.FRIEND, EventOperation.ADD, friendId);
    }

    public void removeFriends(Long userId, Long friendId) {
        findUser(userId);
        findUser(friendId);
        friendService.removeFriend(userId, friendId);
        eventService.createEvent(userId, EventType.FRIEND, EventOperation.REMOVE, friendId);
    }

    public Collection<UserDTO> commonFriends(Long userId1, Long userId2) {
        findUser(userId1);
        findUser(userId2);
        List<Long> user1FriendsIds = new ArrayList<>(friendService.getUserFriends(userId1));
        List<Long> user2FriendsIds = new ArrayList<>(friendService.getUserFriends(userId2));
        List<Long> commonFriendsIds = user1FriendsIds.stream().filter(user2FriendsIds::contains).toList();

        Collection<User> commonFriends = userRepository.findAllByIds(commonFriendsIds);
        return commonFriends.stream().map(UserMapper::mapUserToUserDTO).toList();
    }

    public Collection<UserDTO> getUserFriends(Long userId) {
        findUser(userId);
        Collection<Long> userFriendsIds = friendService.getUserFriends(userId);
        Collection<User> userFriends = userRepository.findAllByIds(userFriendsIds);
        return userFriends.stream().map(UserMapper::mapUserToUserDTO).toList();
    }

    public Collection<UserDTO> getAllUsers() {
        return userRepository.getAllUsers().stream().map(UserMapper::mapUserToUserDTO).toList();
    }

    public UserDTO saveUser(MergeUserRequest userMerge) {
        User user = UserMapper.mapMergeRequestToUser(userMerge);
        long addedUserId = userRepository.addUser(fixUserNameIfNull(user));
        userMerge.setId(addedUserId);
        userMerge.setName(user.getName());
        return UserMapper.mapMergeRequestToUserDTO(userMerge);
    }

    public UserDTO updateUser(MergeUserRequest userMerge) {
        User user = UserMapper.mapMergeRequestToUser(userMerge);
        if (userRepository.updateUser(fixUserNameIfNull(user)) == 0) {
            throw new NotFoundException(String.format("User with id=%s not found", user.getId()));
        }
        userMerge.setName(user.getName());
        return UserMapper.mapMergeRequestToUserDTO(userMerge);
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

    private User fixUserNameIfNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
