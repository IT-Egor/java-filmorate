package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

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

    public void batchInsert(List<Long> directorIds, long filmId) {
        connectingTablesBatchInsert("film_directors",
                "director_id",
                "film_id",
                directorIds,
                filmId);
    }

    public void deleteFilmDirectors(Long filmId) {
        String deleteFilmDirectors = "DELETE FROM film_directors WHERE film_id = ?";
        delete(deleteFilmDirectors, filmId);
    }
}