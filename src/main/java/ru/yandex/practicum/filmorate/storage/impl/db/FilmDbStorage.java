package ru.yandex.practicum.filmorate.storage.impl.db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Optional;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final String insertQuery = "INSERT INTO films (name, description, release_date, duration) VALUES (?, ?, ?, ?)";
    private final String updateQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
    private final String findAllQuery = "SELECT * FROM films";
    private final String findByIdQuery = "SELECT * FROM films WHERE id = ?";

    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbc.query(findAllQuery, mapper);
    }

    @Override
    public Optional<Film> findFilm(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(findByIdQuery, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Film addFilm(Film film) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public Film updateFilm(Film film) {
        throw new IllegalArgumentException("Not implemented");
    }
}
