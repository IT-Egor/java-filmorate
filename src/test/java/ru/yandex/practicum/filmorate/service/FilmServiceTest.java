package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private static FilmService filmService;
    private static UserService userService;
    private static final int USERS_NUMBER = 15;
    private static final int FILMS_NUMBER = 20;

    @BeforeAll
    static void setUpUsers() {
        UserStorage userStorage = new InMemoryUserStorage();
        for (int i = 1; i <= USERS_NUMBER; i++) {
            userStorage.addUser(
                    new User(
                            0,
                            "email" + i + "@mail.ru",
                            "login" + i,
                            "name" + i,
                            LocalDate.now()
                    )
            );
        }
        userService = new UserService(userStorage);

        FilmStorage filmStorage = new InMemoryFilmStorage();
        for (int i = 1; i <= FILMS_NUMBER; i++) {
            filmStorage.addFilm(
                    new Film(
                            0,
                            "name" + i,
                            "description" + i,
                            LocalDate.now(),
                            Duration.ofMinutes(120)
                    )
            );
        }
        filmService = new FilmService(filmStorage, userService);
    }

    @Test
    void shouldAddLike() {
        Film film = filmService.findFilm(1L);
        User user = userService.findUser(1L);
        filmService.addLike(film.getId(), user.getId());
        assertEquals(1, film.getLikesCount());
        assertTrue(film.getLikedUsersIds().contains(user.getId()));
    }

    @Test
    void shouldNotAddLikeIfUserNotFound() {
        Film film = filmService.findFilm(1L);
        film.setLikedUsersIds(new HashSet<>()); // если тест выполнится не в том порядке
        User user = new User(0, "email@mail.ru", "login", "name", LocalDate.now());
        assertThrows(NotFoundException.class, () ->
                filmService.addLike(film.getId(), user.getId()));
        assertEquals(0, film.getLikesCount());
        assertFalse(film.getLikedUsersIds().contains(user.getId()));
    }

    @Test
    void shouldNotAddLikeIfFilmNotFound() {
        Film film = new Film(0,
                "name",
                "description",
                LocalDate.now(),
                Duration.ofMinutes(120));
        User user = userService.findUser(1L);
        assertThrows(NotFoundException.class, () ->
                filmService.addLike(film.getId(), user.getId()));
        assertEquals(0, film.getLikesCount());
        assertFalse(film.getLikedUsersIds().contains(user.getId()));
    }

    @Test
    void shouldRemoveLike() {
        Film film = filmService.findFilm(1L);
        User user = userService.findUser(1L);
        filmService.addLike(film.getId(), user.getId());
        filmService.removeLike(film.getId(), user.getId());
        assertEquals(0, film.getLikesCount());
        assertFalse(film.getLikedUsersIds().contains(user.getId()));
    }

    @Test
    void shouldNotRemoveLikeIfUserNotFound() {
        Film film = filmService.findFilm(1L);
        User user = userService.findUser(1L);
        filmService.addLike(film.getId(), user.getId());
        User unexistingUser = new User(0, "email@mail.ru", "login", "name", LocalDate.now());
        assertThrows(NotFoundException.class, () ->
                filmService.removeLike(film.getId(), unexistingUser.getId()));
        assertEquals(1, film.getLikesCount());
        assertFalse(film.getLikedUsersIds().contains(unexistingUser.getId()));
    }

    @Test
    void shouldNotRemoveLikeIfFilmNotFound() {
        Film film = new Film(0, "name", "description", LocalDate.now(), Duration.ofMinutes(120));
        User user = userService.findUser(1L);
        assertThrows(NotFoundException.class, () ->
                filmService.removeLike(film.getId(), user.getId()));
        assertEquals(0, film.getLikesCount());
        assertFalse(film.getLikedUsersIds().contains(user.getId()));
    }

    @Test
    void shouldReturnMostPopularFilms() {
        int filmsAndUsersDiff = FILMS_NUMBER - USERS_NUMBER;

        for (long i = FILMS_NUMBER; i > 0; i--) {
            for (long j = i - filmsAndUsersDiff; j > 0; j--) {
                Film film = filmService.findFilm(i);
                User user = userService.findUser(j);
                filmService.addLike(film.getId(), user.getId());
            }
        }

        List<Long> mostPopularFilmsIds = filmService.getMostPopularFilms(10).stream().map(Film::getId).toList();
        List<Long> expectedMostPopularFilmsIds = new ArrayList<>(List.of(20L, 19L, 18L, 17L, 16L, 15L, 14L, 13L, 12L, 11L));
        assertEquals(expectedMostPopularFilmsIds, mostPopularFilmsIds);
    }
}