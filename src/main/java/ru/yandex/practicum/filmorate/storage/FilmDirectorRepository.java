package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmDirectorRepository extends BaseRepository<FilmDirector> {
    public FilmDirectorRepository(JdbcTemplate jdbc, RowMapper<FilmDirector> mapper) {
        super(jdbc, mapper);
    }

    public void batchInsert(List<Long> directorsIds, long filmId) {
        try {
            String insert = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
            jdbc.batchUpdate(insert, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, filmId);
                    ps.setLong(2, directorsIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return directorsIds.size();
                }
            });
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation");
        }
    }

    public void deleteFilmDirectors(Long filmId) {
        String deleteFilmGenres = "DELETE FROM film_directors WHERE film_id = ?";
        delete(deleteFilmGenres, filmId);
    }
}