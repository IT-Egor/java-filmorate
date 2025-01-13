package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
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
        return likesService.getMostLikedFilms(filmsSelectionLength).stream()
                .map(this::findFilm)
                .toList();
    }

    public Collection<LikeDTO> getFilmLikes(Long filmId) {
        findFilm(filmId);
        return likesService.getFilmLikes(filmId);
    }

    public FilmDTO saveFilm(FilmDTO filmDTO) {
        try {
            filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
            Film film = FilmMapper.mapToFilm(filmDTO);

            mpaService.findMpaById(film.getMpaId());
            Long addedFilmId = filmRepository.addFilm(film);

            filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().map(GenreDTO::getId).toList(), addedFilmId);

            return findFilm(addedFilmId);
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public FilmDTO updateFilm(FilmDTO filmDTO) {
        Film film;
        try {
            filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
            film = FilmMapper.mapToFilm(filmDTO);

            mpaService.findMpaById(film.getMpaId());
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        if (filmRepository.updateFilm(film) == 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", filmDTO.getId()));
        }

        filmGenreService.deleteFilmGenres(filmDTO.getId());
        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().map(GenreDTO::getId).toList(), filmDTO.getId());

        return findFilm(filmDTO.getId());
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmRepository.getAllFilms().stream().map(film -> {
            Mpa mpa = mpaService.findMpaById(film.getMpaId());

            List<Genre> genres = filmGenreService.getGenresByFilmId(film.getId());

            return FilmMapper.mapToFilmDTO(film, genres, mpa);
        }).toList();
    }

    public FilmDTO findFilm(Long id) {
        Optional<Film> filmOpt = filmRepository.findFilm(id);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();

            Mpa mpa = mpaService.findMpaById(film.getMpaId());

            List<Genre> genres = filmGenreService.getGenresByFilmId(id);

            return FilmMapper.mapToFilmDTO(film, genres, mpa);
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

    public void removeFilm(Long filmId) {
        findFilm(filmId);
        filmRepository.removeFilm(filmId);
    }
}
