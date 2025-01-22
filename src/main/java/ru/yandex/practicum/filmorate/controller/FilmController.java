package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.annotations.SearchFilters;
import ru.yandex.practicum.filmorate.validator.annotations.PopularFilmsFilters;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private static class ParamConstants {
        public static final String countMapParam = "count";
        public static final String defaultCountParamValue = "10";
        public static final String searchQueryParam = "query";
        public static final String searchByConstant = "by";
        public static final String searchByDelimiter = ",";
    }

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
    public FilmDTO addFilm(@Valid @RequestBody FilmDTO filmDTO) {
        return filmService.saveFilm(filmDTO);
    }

    @PutMapping
    public FilmDTO updateFilm(@Valid @RequestBody FilmDTO filmDTO) {
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
    public Collection<FilmDTO> getMostPopularFilms(@Valid
                                                   @PopularFilmsFilters
                                                   @RequestParam Map<String, String> params) {
        params.putIfAbsent(ParamConstants.countMapParam, ParamConstants.defaultCountParamValue);
        return filmService.getMostPopularFilms(params);
    }

    @GetMapping("/search")
    public Collection<FilmDTO> searchFilms(@Valid
                                           @SearchFilters
                                           @RequestParam
                                           Map<String, String> params) {

        return filmService.searchFilms(params.get(ParamConstants.searchQueryParam),
                List.of(params.get(ParamConstants.searchByConstant).split(ParamConstants.searchByDelimiter)));
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmDTO> getFilmsByDirector(
            @PathVariable Long directorId,
            @RequestParam(required = false, defaultValue = "year") String sortBy
    ) {
        return filmService.getFilmsByDirectorId(directorId, sortBy);
    }

    @GetMapping("/{filmId}/likes")
    public Collection<LikeDTO> getLikes(@PathVariable Long filmId) {
        return filmService.getFilmLikes(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void removeFilm(@PathVariable Long filmId) {
        filmService.removeFilm(filmId);
    }

    @GetMapping("/common")
    public List<FilmDTO> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
