package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDTO> getFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDTO getFilm(@PathVariable Long id) {
        return filmService.findFilm(id);
    }

    @PostMapping
    public FilmDTO addFilm(@RequestBody FilmDTO filmDTO) {
        return filmService.saveFilm(filmDTO);
    }

    @PutMapping
    public FilmDTO updateFilm(@RequestBody FilmDTO filmDTO) {
        return filmService.updateFilm(filmDTO);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDTO> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10", value = "count") int count) {
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/{filmId}/likes")
    public Collection<LikeDTO> getLikes(@PathVariable Long filmId) {
        return filmService.getFilmLikes(filmId);
    }
}
