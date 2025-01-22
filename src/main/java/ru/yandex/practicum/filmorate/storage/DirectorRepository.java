package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class DirectorRepository extends BaseRepository<Director> {
    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Director> getAllDirectors() {
        String selectAll = "SELECT * FROM directors";
        return findMany(selectAll);
    }

    public Optional<Director> findById(Long id) {
        String selectById = "SELECT * FROM directors WHERE id = ?";
        return findOne(selectById, id);
    }

    public Long addDirector(Director director) {
        String insert = "INSERT INTO directors (name) VALUES (?)";
        return insert(insert, director.getName());
    }

    public Long updateDirector(Director director) {
        String update = "UPDATE directors SET name = ? WHERE id = ?";
        return update(update, director.getName(), director.getId());
    }

    public void removeDirector(Long directorId) {
        String delete = "DELETE FROM directors WHERE id = ?";
        delete(delete, directorId);
    }

    public Collection<Director> findAllByFilmId(Long filmId) {
        String selectAllByFilmId = """
                SELECT d.*
                FROM directors d
                INNER JOIN film_directors fd ON d.id = fd.director_id
                WHERE fd.film_id = ?
                """;
        return findMany(selectAllByFilmId, filmId);
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