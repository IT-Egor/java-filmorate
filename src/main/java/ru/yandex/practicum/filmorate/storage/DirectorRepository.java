package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
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
}