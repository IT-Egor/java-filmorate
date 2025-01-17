package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class LikeRepository extends BaseRepository<Like> {
    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Like> getFilmLikes(Long filmId) {
        String selectFilmLikes = "SELECT * FROM likes WHERE film_id = ?";
        return findMany(selectFilmLikes, filmId);
    }

    public long addLikeToFilm(Long filmId, Long userId) {
        String insert = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        return insert(insert, filmId, userId);
    }

    public boolean removeLikeFromFilm(Long filmId, Long userId) {
        String delete = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        return delete(delete, filmId, userId);
    }

    public List<Long> getMostLikedFilmsIds(int selectionLimit) {
        String selectMostLikedFilms =
                "SELECT film_id " +
                        "FROM likes " +
                        "GROUP BY film_id " +
                        "ORDER BY COUNT(*) DESC " +
                        "LIMIT ?";
        return jdbc.queryForList(selectMostLikedFilms, Long.class, selectionLimit);
    }

    public List<Long> getMutualFilm(Long userId, Long friendId) {
        String sql =
                "SELECT l.film_id " +
                        "FROM likes l " +
                        "WHERE l.film_id IN (" +
                        "    SELECT film_id " +
                        "    FROM likes " +
                        "    WHERE user_id = ? " +
                        ") AND l.film_id IN (" +
                        "    SELECT film_id " +
                        "    FROM likes " +
                        "    WHERE user_id = ? " +
                        ") " +
                        "GROUP BY l.film_id " +
                        "ORDER BY COUNT(l.user_id) DESC;";

        return jdbc.queryForList(sql, Long.class, userId, friendId);
    }

    public Set<Long> getLikedMovies(Long userId) {
        String sqlForMovies = "SELECT film_id FROM likes WHERE user_id = ?";
        Set<Long> movies = new HashSet<>(jdbc.queryForList(sqlForMovies, Long.class, userId));

        String sqlForUser = "SELECT user_id" +
                " FROM likes" +
                " WHERE film_id IN (SELECT film_id FROM likes WHERE user_id = ?) AND user_id != ?";
        Set<Long> users = new HashSet<>(jdbc.queryForList(sqlForUser, Long.class, userId, userId));

        if (users.isEmpty()) {
            return new HashSet<>();
        }

        StringBuilder lastSql = new StringBuilder("SELECT film_id FROM likes WHERE user_id IN (");
        for (Long user : users) {
            lastSql.append(user).append(",");
        }
        lastSql.setLength(lastSql.length() - 1);
        lastSql.append(") AND film_id NOT IN (");
        for (Long filmId : movies) {
            lastSql.append(filmId).append(",");
        }
        lastSql.setLength(lastSql.length() - 1);
        lastSql.append(")");
        return new HashSet<>(jdbc.queryForList(String.valueOf(lastSql), Long.class));
    }
}
