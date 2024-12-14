package ru.yandex.practicum.filmorate.storage.impl.db;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class FilmGenreDbStorage extends BaseDbStorage<FilmGenre> {
    public FilmGenreDbStorage(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public void batchInsert(List<GenreDTO> genreDTOList, long filmId) {
        try {
            String insert = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            jdbc.batchUpdate(insert, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genreDTOList.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genreDTOList.size();
                }
            });
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation");
        }
    }

    public Collection<FilmGenre> findAllByFilmId(Long filmId) {
        String selectAllByFilmId = "SELECT * FROM film_genres WHERE film_id = ?";
        return findMany(selectAllByFilmId, filmId);
    }
}
