package ru.yandex.practicum.filmorate.utility;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

public class Validator {

    private Validator() {}

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Film name cannot be empty");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film description cannot be longer than 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Film release date cannot be earlier than 28.12.1895");
        }
        if (!film.getDuration().isPositive()) {
            throw new ValidationException("Film duration should be positive");
        }
    }

    public static User validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("The specified address does not match the template");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot be empty and contain spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday cannot be in future");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            User fixedUser = new User();
            fixedUser.setId(user.getId());
            fixedUser.setName(user.getLogin());
            fixedUser.setBirthday(user.getBirthday());
            fixedUser.setLogin(user.getLogin());
            fixedUser.setEmail(user.getEmail());
//            throw new ValidationException("Username cannot be empty");
            return fixedUser;
        }
        return user;
    }
}
