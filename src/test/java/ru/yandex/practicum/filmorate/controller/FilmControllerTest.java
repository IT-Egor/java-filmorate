package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film(0, "test", "description", LocalDate.now(), Duration.ofHours(1));
    }

    @Test
    void shouldAddFilm() {
        filmController.addFilm(film);
        assertEquals(filmController.getFilms().toString(), List.of(film).toString());
    }

    @Test
    void shouldGenerateCorrectId() {
        filmController.addFilm(film);
        filmController.addFilm(new Film(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()));
        filmController.addFilm(new Film(film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()));

        List<Long> ids = filmController.getFilms().stream().map(Film::getId).toList();
        assertEquals(ids.get(0), ids.get(1) - 1);
        assertEquals(ids.get(1), ids.get(2) - 1);
    }

    @Test
    void shouldUpdateFilm() {
        filmController.addFilm(film);
        Film updatedFilm = new Film(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        updatedFilm.setName("updatedFilm");

        filmController.updateFilm(updatedFilm);
        assertEquals(filmController.getFilms().toString(), List.of(updatedFilm).toString());
    }

    @Test
    void shouldReturnEmptyListWhenNoFilmsFound() {
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void shouldNotUpdateUnexistingFilm() {
        filmController.addFilm(film);
        Film updatedFilm = new Film(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        updatedFilm.setId(0);

        assertThrows(NotFoundException.class, () -> filmController.updateFilm(updatedFilm));
    }
}