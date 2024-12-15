package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findById(Long id) {
        String selectOne = "SELECT * FROM ratings WHERE id = ?";
        return findOne(selectOne, id);
    }

    public Collection<Mpa> findAll() {
        String selectAll = "SELECT * FROM ratings";
        return findMany(selectAll);
    }
}
