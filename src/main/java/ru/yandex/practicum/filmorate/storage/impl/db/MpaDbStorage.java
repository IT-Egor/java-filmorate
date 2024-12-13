package ru.yandex.practicum.filmorate.storage.impl.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> {
    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findById(Long id) {
        String selectOne = "SELECT * FROM ratings WHERE id = ?";
        return findOne(selectOne, id);
    }
}
