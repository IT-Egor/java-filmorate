package ru.yandex.practicum.filmorate.storage.impl.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Optional;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        String findAllQuery = "SELECT * FROM films";
        return findMany(findAllQuery);
    }

    @Override
    public Optional<Film> findFilm(long id) {
        String findByIdQuery = "SELECT * FROM films WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    @Override
    public Film addFilm(Film film) {
        String insertQuery = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        long id = merge(insertQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa().getId());
        return findFilm(id).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    @Override
    public Film updateFilm(Film film) {
        String updateQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        long id = merge(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa().getId(),
                film.getId());
        return findFilm(id).orElseThrow(() -> new NotFoundException("Film not found"));
    }
}
