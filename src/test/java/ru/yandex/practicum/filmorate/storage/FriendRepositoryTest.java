package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.mapper.FriendRowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FriendRepository.class, FriendRowMapper.class})
class FriendRepositoryTest {
    private final FriendRepository friendRepository;

    @Test
    void getUserFriends() {
        List<Friend> expected = List.of(new Friend(1L, 1L, 2L), new Friend(2L, 1L, 3L));
        List<Friend> actual = new ArrayList<>(friendRepository.getUserFriends(1L));
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void addFriend() {
        Friend friend = new Friend(1L, 3L, 1L);
        friend.setId(friendRepository.addFriend(3L, 1L));
        assertTrue(friendRepository.getUserFriends(3L).contains(friend));
    }

    @Test
    void removeFriend() {
        friendRepository.removeFriend(1L, 2L);
        assertFalse(friendRepository.getUserFriends(1L).contains(new Friend(1L, 1L, 2L)));
    }
}