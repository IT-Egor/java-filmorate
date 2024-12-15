package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.impl.db.FriendDbStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FriendService {
    private final FriendDbStorage friendDbStorage;

    public void addFriend(Long userId, Long friendId) {
        friendDbStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        friendDbStorage.removeFriend(userId, friendId);
    }

    public Collection<Long> getUserFriends(Long userId) {
        return friendDbStorage.getUserFriends(userId).stream().map(Friend::getFriendId).toList();
    }
}
