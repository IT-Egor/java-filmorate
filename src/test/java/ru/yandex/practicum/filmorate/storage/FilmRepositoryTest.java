package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class})
class FilmRepositoryTest {
    private final FilmRepository filmRepository;

    @Test
    void getAllFilms() {
        List<Film> expected = List.of(
                new Film(
                        1,
                        "Фильм 1",
                        "Описание фильма 1",
                        LocalDate.of(2020, 1, 1),
                        Duration.ofMinutes(120),
                        1L,
                        0L),
                new Film(
                        2,
                        "Фильм 2",
                        "Описание фильма 2",
                        LocalDate.of(2019, 6, 15),
                        Duration.ofMinutes(90),
                        2L,
                        0L),
                new Film(3,
                        "Фильм 3",
                        "Описание фильма 3",
                        LocalDate.of(2018, 3, 20),
                        Duration.ofMinutes(150),
                        3L,
                        0L)
        );
        assertArrayEquals(expected.toArray(), filmRepository.getAllFilms().toArray());
    }

    @Test
    void findFilm() {
        Film expected = new Film(
                1,
                "Фильм 1",
                "Описание фильма 1",
                LocalDate.of(2020, 1, 1),
                Duration.ofMinutes(120),
                1L,
                0L);
        assertEquals(expected, filmRepository.findFilm(1).get());
    }

    @Test
    void addFilm() {
        Film film = new Film(
                4,
                "Фильм 4",
                "Описание фильма 4",
                LocalDate.of(2020, 1, 1),
                Duration.ofMinutes(120),
                1L,
                0L);
        film.setId(filmRepository.addFilm(film));
        assertEquals(film, filmRepository.findFilm(4).get());
    }

    @Test
    void updateFilm() {
        Film film = new Film(
                4,
                "Фильм 4",
                "Описание фильма 4",
                LocalDate.of(2020, 1, 1),
                Duration.ofMinutes(120),
                1L,
                0L);
        film.setId(filmRepository.addFilm(film));

        Film updatedFilm = new Film(
                film.getId(),
                "Обновленный фильм 4",
                "Обновленное описание фильма 4",
                LocalDate.of(2021, 2, 3),
                Duration.ofMinutes(180),
                2L,
                0L);
        filmRepository.updateFilm(updatedFilm);
        assertEquals(updatedFilm, filmRepository.findFilm(film.getId()).get());
    }
}