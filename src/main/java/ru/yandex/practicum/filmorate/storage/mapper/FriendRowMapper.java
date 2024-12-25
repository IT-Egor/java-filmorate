package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendRowMapper implements RowMapper<Friend> {
    @Override
    public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friend friend = new Friend();
        friend.setId(rs.getLong("id"));
        friend.setFriendId(rs.getLong("friend_id"));
        friend.setUserId(rs.getLong("user_id"));
        return friend;
    }
}
