package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        int id = nextId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    private int nextId() {
        int maxId = films.values().stream()
                .map(Film::getId)
                .max(Integer::compareTo)
                .orElse(0);
        return ++maxId;
    }
}
