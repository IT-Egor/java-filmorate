package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> getAllFilms() {
        String findAllQuery = "SELECT * FROM films";
        return findMany(findAllQuery);
    }

    public Optional<Film> findFilm(long id) {
        String findByIdQuery = "SELECT * FROM films WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    public Long addFilm(Film film) {
        String insertQuery = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        return insert(insertQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpaId());
    }

    public Long updateFilm(Film film) {
        String updateQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        return update(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpaId(),
                film.getId());
    }
}
