package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.FriendRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public void addFriend(Long userId, Long friendId) {
        friendRepository.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        friendRepository.removeFriend(userId, friendId);
    }

    public Collection<Long> getUserFriends(Long userId) {
        return friendRepository.getUserFriends(userId).stream().map(Friend::getFriendId).toList();
    }
}
