package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmGenreService {
    private final FilmGenreRepository filmGenreRepository;
    private final GenreService genreService;

    public void addGenresToFilm(List<GenreDTO> genres, Long filmId) {
        filmGenreRepository.batchInsert(genres, filmId);
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        List<FilmGenre> filmGenres = new ArrayList<>(filmGenreRepository.findAllByFilmId(filmId));
        return filmGenres.stream().map(filmGenre ->
                genreService.findGenreById(filmGenre.getGenreId())
        ).toList();
    }

    public boolean deleteFilmGenres(Long filmId) {
        return filmGenreRepository.deleteFilmGenres(filmId);
    }
}
