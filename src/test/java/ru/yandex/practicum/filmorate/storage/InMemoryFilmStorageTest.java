package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.memory.InMemoryFilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {

    private FilmStorage filmStorage;
    private Film film;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        film = new Film(0, "test", "description", LocalDate.now(), Duration.ofHours(1));
    }

    @Test
    void shouldAddFilm() {
        filmStorage.addFilm(film);
        assertEquals(filmStorage.getAllFilms().toString(), List.of(film).toString());
    }

    @Test
    void shouldGenerateCorrectId() {
        filmStorage.addFilm(film);
        filmStorage.addFilm(new Film(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()));
        filmStorage.addFilm(new Film(film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()));

        List<Long> ids = filmStorage.getAllFilms().stream().map(Film::getId).toList();
        assertEquals(ids.get(0), ids.get(1) - 1);
        assertEquals(ids.get(1), ids.get(2) - 1);
    }

    @Test
    void shouldUpdateFilm() {
        filmStorage.addFilm(film);
        Film updatedFilm = new Film(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        updatedFilm.setName("updatedFilm");

        filmStorage.updateFilm(updatedFilm);
        assertEquals(filmStorage.getAllFilms().toString(), List.of(updatedFilm).toString());
    }

    @Test
    void shouldReturnEmptyListWhenNoFilmsFound() {
        assertEquals(0, filmStorage.getAllFilms().size());
    }

    @Test
    void shouldNotUpdateUnexistingFilm() {
        filmStorage.addFilm(film);
        Film updatedFilm = new Film(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        updatedFilm.setId(0);

        assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(updatedFilm));
    }
}