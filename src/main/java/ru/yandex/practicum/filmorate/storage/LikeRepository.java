package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

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

    public Collection<Like> getAllFilmsLikes() {
        String selectAll = "SELECT * FROM likes";
        return findMany(selectAll);
    }
}
