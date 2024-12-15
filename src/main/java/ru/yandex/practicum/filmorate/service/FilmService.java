package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.utility.Validator;

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
        Map<Long, Long> filmsLikes = likesService.getFilmsLikesCount();
        List<Long> mostPopularFilms = filmsLikes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(filmsSelectionLength)
                .toList();
        return mostPopularFilms.stream().map(this::findFilm).toList();
    }

    public Collection<LikeDTO> getFilmLikes(Long filmId) {
        findFilm(filmId);
        return likesService.getFilmLikes(filmId);
    }

    public FilmDTO saveFilm(FilmDTO filmDTO) {
        filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
        Film film = FilmMapper.mapToFilm(filmDTO);
        Long addedFilmId = filmRepository.addFilm(film);

        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().toList(), addedFilmId);

        return findFilm(addedFilmId);
    }

    public FilmDTO updateFilm(FilmDTO filmDTO) {
        filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
        Film film = FilmMapper.mapToFilm(filmDTO);
        if (filmRepository.updateFilm(film) == 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", filmDTO.getId()));
        }

        filmGenreService.deleteFilmGenres(filmDTO.getId());
        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().toList(), filmDTO.getId());

        return findFilm(filmDTO.getId());
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmRepository.getAllFilms().stream().map(film -> {
            Mpa mpa = mpaService.findMpaById(film.getMpa().getId());
            film.setMpa(mpa);

            List<Genre> genres = filmGenreService.getGenresByFilmId(film.getId());
            film.setGenres(genres);

            return FilmMapper.mapToFilmDTO(film);
        }).toList();
    }

    public FilmDTO findFilm(Long id) {
        Optional<Film> filmOpt = filmRepository.findFilm(id);
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
