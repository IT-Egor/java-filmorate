package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.Collection;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);
        film.addLike(user);
    }

    public void removeLike(Long filmId, Long userId) {
        User user = userStorage.findUser(userId);
        Film film = filmStorage.findFilm(filmId);
        film.removeLike(user);
    }

    public Collection<Film> getMostPopularFilms(int filmsSelectionLength) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(filmsSelectionLength)
                .toList();
    }

    public Film saveFilm(Film film) {
        Validator.validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        Validator.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }
}
