package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserService userService;
    private final MpaService mpaService;
    private final FilmGenreService filmGenreService;
    private final FilmDirectorService filmDirectorService;
    private final GenreService genreService;
    private final DirectorService directorService;
    private final LikesService likesService;

    public void addLike(Long filmId, Long userId) {
        userService.findUser(userId);
        FilmDTO film = findFilm(filmId);
        likesService.addLike(filmId, userId);

        film.setLikes(film.getLikes() + 1);
        updateFilm(film);
    }

    public void removeLike(Long filmId, Long userId) {
        userService.findUser(userId);
        FilmDTO film = findFilm(filmId);

        if (!likesService.removeLike(filmId, userId)) {
            throw new BadRequestException(String.format("Film with id=%s already unliked by user with id=%s", filmId, userId));
        } else {
            film.setLikes(film.getLikes() - 1);
            updateFilm(film);
        }
    }

    public Collection<FilmDTO> getMostPopularFilms(int filmsSelectionLength) {
        Collection<FilmDTO> allFilms = getAllFilms();
        Collection<FilmDTO> likeFilms = new java.util.ArrayList<>(likesService.getMostLikedFilms(filmsSelectionLength).stream()
                .map(this::findFilm)
                .toList());

        if (filmsSelectionLength > likeFilms.size()) {
            for (FilmDTO film : allFilms) {
                if (!likeFilms.contains(film)) {
                    likeFilms.add(film);
                }
                if (filmsSelectionLength == likeFilms.size()) {
                    return likeFilms;
                }
            }
        }
        return likeFilms;
    }

    public Collection<LikeDTO> getFilmLikes(Long filmId) {
        findFilm(filmId);
        return likesService.getFilmLikes(filmId);
    }

    public FilmDTO saveFilm(FilmDTO filmDTO) {
        try {
            filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
            filmDTO.setDirectors(directorService.fixIfNullOrWithDuplicates(filmDTO.getDirectors()));
            Film film = FilmMapper.mapToFilm(filmDTO);

            mpaService.findMpaById(film.getMpaId());
            Long addedFilmId = filmRepository.addFilm(film);

            filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().map(GenreDTO::getId).toList(), addedFilmId);
            filmDirectorService.addDirectorsToFilm(filmDTO.getDirectors().stream().map(DirectorDTO::getId).toList(), addedFilmId);

            return findFilm(addedFilmId);
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public FilmDTO updateFilm(FilmDTO filmDTO) {
        Film film;
        try {
            filmDTO.setGenres(genreService.fixIfNullOrWithDuplicates(filmDTO.getGenres()));
            filmDTO.setDirectors(directorService.fixIfNullOrWithDuplicates(filmDTO.getDirectors()));
            film = FilmMapper.mapToFilm(filmDTO);

            mpaService.findMpaById(film.getMpaId());
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        if (filmRepository.updateFilm(film) == 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", filmDTO.getId()));
        }

        filmGenreService.addGenresToFilm(filmDTO.getGenres().stream().map(GenreDTO::getId).toList(), filmDTO.getId());
        filmDirectorService.addDirectorsToFilm(filmDTO.getDirectors().stream().map(DirectorDTO::getId).toList(), filmDTO.getId());

        return findFilm(filmDTO.getId());
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmRepository.getAllFilms().stream().map(film -> {
            Mpa mpa = mpaService.findMpaById(film.getMpaId());

            List<Genre> genres = filmGenreService.getGenresByFilmId(film.getId());
            List<Director> directors = filmDirectorService.getDirectorsByFilmId(film.getId());

            return FilmMapper.mapToFilmDTO(film, genres, directors, mpa);
        }).toList();
    }

    public FilmDTO findFilm(Long id) {
        Optional<Film> filmOpt = filmRepository.findFilm(id);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();

            Mpa mpa = mpaService.findMpaById(film.getMpaId());

            List<Genre> genres = filmGenreService.getGenresByFilmId(id);
            List<Director> directors = directorService.getDirectorsByFilmId(id);

            return FilmMapper.mapToFilmDTO(film, genres, directors, mpa);
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

    public void removeFilm(Long filmId) {
        findFilm(filmId);
        filmRepository.removeFilm(filmId);
    }

    public Collection<FilmDTO> getFilmsByDirectorId(Long directorId, String sortBy) {
        List<FilmDirector> filmDirectors = new ArrayList<>(filmDirectorService.findAllByDirectorId(directorId));
        if (sortBy.equals("year")) {
            return filmDirectors.stream()
                    .map(filmDirector -> findFilm(filmDirector.getFilmId()))
                    .sorted(Comparator.comparing(FilmDTO::getReleaseDate))
                    .toList();
        } else {
            return filmDirectors.stream()
                    .map(filmDirector -> findFilm(filmDirector.getFilmId()))
                    .sorted(Comparator.comparing(FilmDTO::getLikes).reversed())
                    .toList();
        }
    }
}
