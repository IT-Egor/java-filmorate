package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
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
    private final LikesService likesService;

    public void addLike(Long filmId, Long userId) {
        userService.findUser(userId);
        findFilm(filmId);
        likesService.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userService.findUser(userId);
        findFilm(filmId);
        if (!likesService.removeLike(filmId, userId)) {
            throw new BadRequestException(String.format("Film with id=%s already unliked by user with id=%s", filmId, userId));
        }
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

        film.setLikes(likesService.getFilmLikes(addedFilmId));

        return findFilm(addedFilmId);
    }

    public FilmDTO updateFilm(FilmDTO filmDTO) {
        filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
        Film film = FilmMapper.mapToFilm(filmDTO);
        Validator.validateFilm(film);
        if (filmStorage.updateFilm(film) == 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", filmDTO.getId()));
        }

        filmGenreService.deleteFilmGenres(filmDTO.getId());
        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().toList(), filmDTO.getId());

        film.setLikes(likesService.getFilmLikes(filmDTO.getId()));

        return findFilm(filmDTO.getId());
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmStorage.getAllFilms().stream().map(film -> {
            Mpa mpa = mpaService.findMpaById(film.getMpa().getId());
            film.setMpa(mpa);

            List<Genre> genres = filmGenreService.getGenresByFilmId(film.getId());
            film.setGenres(genres);

            film.setLikes(likesService.getFilmLikes(film.getId()));

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

            film.setLikes(likesService.getFilmLikes(id));

            return FilmMapper.mapToFilmDTO(film);
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }
}
