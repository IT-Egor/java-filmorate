package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        Validator.validateFilm(film);
        long id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("Film with id {} added", id);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Validator.validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Film with id %d not found", film.getId()));
        }
        films.put(film.getId(), film);
        log.info("Film with id {} updated", film.getId());
        return film;
    }


    private long getNextId() {
        long maxId = films.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return ++maxId;
    }
}
