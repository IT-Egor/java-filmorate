package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Collection;

@Repository
public class FriendRepository extends BaseRepository<Friend> {
    public FriendRepository(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Friend> getUserFriends(Long userId) {
        String selectUserFriends = "SELECT * FROM friends WHERE user_id = ?";
        return findMany(selectUserFriends, userId);
    }

    public long addFriend(Long userId, Long friendId) {
        String insertFriend = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        return insert(insertFriend, userId, friendId);
    }

    public boolean removeFriend(Long userId, Long friendId) {
        String deleteFriend = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        return delete(deleteFriend, userId, friendId);
    }
}
