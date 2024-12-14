package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.*;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {
    private final @Qualifier("filmDbStorage") FilmStorage filmStorage;
    private final UserService userService;
    private final MpaService mpaService;
    private final FilmGenreService filmGenreService;
    private final GenreService genreService;

    public void addLike(Long filmId, Long userId) {
//        FilmDTO filmDTO = findFilm(filmId);
//        User user = userService.findUser(userId);
//        filmDTO.addLike(user);
        throw new RuntimeException("Not implemented");
    }

    public void removeLike(Long filmId, Long userId) {
//        User user = userService.findUser(userId);
//        FilmDTO filmDTO = findFilm(filmId);
//        filmDTO.removeLike(user);
        throw new RuntimeException("Not implemented");
    }

    public Collection<FilmDTO> getMostPopularFilms(int filmsSelectionLength) {
//        return filmStorage.getAllFilms().stream()
//                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
//                .limit(filmsSelectionLength)
//                .map(FilmMapper::mapToFilmDTO)
//                .toList();
        throw new RuntimeException("Not implemented");
    }

    public FilmDTO saveFilm(FilmDTO filmDTO) {
        filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
        Film film = FilmMapper.mapToFilm(filmDTO);
        Validator.validateFilm(film);
        Long addedFilmId = filmStorage.addFilm(film);

        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().toList(), addedFilmId);

        return findFilm(addedFilmId);
    }

    public FilmDTO updateFilm(FilmDTO filmDTO) {
        filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
        Film film = FilmMapper.mapToFilm(filmDTO);
        Validator.validateFilm(film);
        if (filmStorage.updateFilm(film) == 0) {
            throw new InternalServerException("Failed to update film");
        }

        filmGenreService.deleteFilmGenres(filmDTO.getId());
        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().toList(), filmDTO.getId());

        return findFilm(filmDTO.getId());
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmStorage.getAllFilms().stream().map(film -> {
            Mpa mpa = mpaService.findMpaById(film.getMpa().getId());
            film.setMpa(mpa);

            List<Genre> genres = filmGenreService.getGenresByFilmId(film.getId());
            film.setGenres(genres);

            return FilmMapper.mapToFilmDTO(film);
        }).toList();
    }

    public FilmDTO findFilm(Long id) {
        Optional<Film> filmOpt = filmStorage.findFilm(id);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();

            Mpa mpa = mpaService.findMpaById(film.getMpa().getId());
            film.setMpa(mpa);

            List<Genre> genres = filmGenreService.getGenresByFilmId(id);
            film.setGenres(genres);

            return FilmMapper.mapToFilmDTO(film);
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }
}
