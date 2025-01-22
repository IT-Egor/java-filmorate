package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> findById(Long id) {
        String selectById = "SELECT * FROM genres WHERE id = ?";
        return findOne(selectById, id);
    }

    public Collection<Genre> findAll() {
        String selectAll = "SELECT * FROM genres";
        return findMany(selectAll);
    }

    public Collection<Genre> getGenresByFilmId(Long filmId) {
        String selectAllByFilmId = """
                SELECT g.*
                FROM genres g
                INNER JOIN film_genres fg ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;
        return findMany(selectAllByFilmId, filmId);
    }

    public void batchInsert(List<Long> genresIds, long filmId) {
        try {
            String insert = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            jdbc.batchUpdate(insert, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genresIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return genresIds.size();
                }
            });
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation");
        }
    }

    public boolean deleteFilmGenres(Long filmId) {
        String deleteFilmGenres = "DELETE FROM film_genres WHERE film_id = ?";
        return delete(deleteFilmGenres, filmId);
    }
}
