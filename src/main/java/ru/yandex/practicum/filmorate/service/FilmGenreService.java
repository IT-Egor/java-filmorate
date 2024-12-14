package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmGenreDbStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmGenreService {
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final GenreService genreService;

    public void addGenresToFilm(List<GenreDTO> genres, Long filmId) {
        filmGenreDbStorage.batchInsert(genres, filmId);
    }

    public Set<Genre> getGenresByFilmId(Long filmId) {
        Set<FilmGenre> filmGenres = new HashSet<>(filmGenreDbStorage.findAllByFilmId(filmId));
        Set<Genre> genresDTO = filmGenres.stream().map(filmGenre ->
            genreService.findGenreById(filmGenre.getGenreId())
        ).collect(Collectors.toSet());
        return genresDTO;
    }
}
