package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @GetMapping
    public Collection<Film> getFilms() {
        throw new RuntimeException("Not implemented");
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        throw new RuntimeException("Not implemented");
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        throw new RuntimeException("Not implemented");
    }
}
