package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;
import java.util.List;

@Repository
public class LikeRepository extends BaseRepository<Like> {
    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Like> getFilmLikes(Long filmId) {
        String selectFilmLikes = "SELECT * FROM likes WHERE film_id = ?";
        return findMany(selectFilmLikes, filmId);
    }

    public Long getFilmLikesCount(Long filmId) {
        String selectFilmLikesCount = "SELECT COUNT(*) FROM likes WHERE film_id = ?";
        return jdbc.queryForObject(selectFilmLikesCount, Long.class, filmId);
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        String insert = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        insert(insert, filmId, userId);
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

    public List<Long> getCommonFilms(Long userId, Long friendId) {
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
}
