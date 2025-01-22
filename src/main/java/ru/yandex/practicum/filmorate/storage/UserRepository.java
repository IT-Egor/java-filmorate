package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Collection<User> getAllUsers() {
        String selectAll = "SELECT * FROM users";
        return findMany(selectAll);
    }

    public Long addUser(User user) {
        String insert = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        return insert(insert, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    public Long updateUser(User user) {
        String update = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        return update(update, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
    }

    public Optional<User> findUser(long id) {
        String selectOne = "SELECT * FROM users WHERE id = ?";
        return findOne(selectOne, id);
    }

    public void removeUser(Long id) {
        String deleteUserQuery = "DELETE FROM users WHERE id = ?";
        update(deleteUserQuery, id);
    }

    public Collection<User> findAllByIds(Collection<Long> userIds) {
        String inSql = String.join(",", Collections.nCopies(userIds.size(), "?"));
        String selectAllByIds = String.format("""
                SELECT * FROM users
                WHERE id IN (%s)
                """, inSql);
        return findMany(selectAllByIds, userIds.toArray());
    }
}
