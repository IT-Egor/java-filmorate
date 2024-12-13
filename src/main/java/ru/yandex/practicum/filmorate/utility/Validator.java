package ru.yandex.practicum.filmorate.utility;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class Validator {

    private Validator() {

    }

    public static void validateFilm(Film film) {
        if (film.getName() == null
                || film.getDescription() == null
                || film.getDuration() == null
                || film.getReleaseDate() == null) {
            log.error("Film have null values");
            throw new ValidationException("Film have null values");
        }
        if (film.getName().isBlank()) {
            log.error("Film name is null or empty");
            throw new ValidationException("Film name cannot be empty");
        }
        if (film.getDescription().length() > 200) {
            log.error("Film description is too long");
            throw new ValidationException("Film description cannot be longer than 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Film release date is too old");
            throw new ValidationException("Film release date cannot be earlier than 28.12.1895");
        }
        if (!film.getDuration().isPositive()) {
            log.error("Film duration is not positive");
            throw new ValidationException("Film duration should be positive");
        }
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            log.error("Film mpa is null");
            throw new ValidationException("Film mpa is null");
        }
    }

    public static void validateUser(User user) {
        if (user.getLogin() == null
                || user.getEmail() == null
                || user.getBirthday() == null) {
            log.error("User has null values");
            throw new ValidationException("User has null values");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Email is not valid");
            throw new ValidationException("Email is not valid");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Login is not valid");
            throw new ValidationException("Login cannot be empty and contain spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error(("Birthday is in the future"));
            throw new ValidationException("Birthday cannot be in future");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("The username was changed to login '{}'", user.getLogin());
        }
    }
}
