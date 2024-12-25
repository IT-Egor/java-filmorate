package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
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
}
