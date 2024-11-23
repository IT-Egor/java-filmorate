package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public void addLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);
        User user = userService.findUser(userId);
        film.addLike(user);
    }

    public void removeLike(Long filmId, Long userId) {
        User user = userService.findUser(userId);
        Film film = findFilm(filmId);
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

    public Film findFilm(Long id) {
        Optional<Film> filmOpt = filmStorage.findFilm(id);
        if (filmOpt.isPresent()) {
            return filmOpt.get();
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }
}
