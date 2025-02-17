package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final DirectorService directorService;
    private final LikesService likesService;
    private final EventService eventService;

    public void addLike(Long filmId, Long userId) {
        userService.findUser(userId);
        findFilm(filmId);
        likesService.addLike(filmId, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.ADD, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        userService.findUser(userId);
        findFilm(filmId);

        if (!likesService.removeLike(filmId, userId)) {
            throw new BadRequestException(String.format("Film with id=%s already unliked by user with id=%s", filmId, userId));
        } else {
            eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
        }
    }

    public Collection<FilmDTO> getMostPopularFilms(Map<String, String> searchFilters) {
        return mapFilmsToFilmsDTO(filmRepository.getPopularFilms(searchFilters));
    }

    public Collection<FilmDTO> searchFilms(String query, List<String> searchFilters) {
        Collection<Film> films;
        if (searchFilters.size() == 1) {
            if (searchFilters.contains("title")) {
                films = findFilmsByTitle(query);
            } else if (searchFilters.contains("director")) {
                films = findFilmsByDirectorName(query);
            } else {
                throw new BadRequestException("Unknown search filter");
            }
        } else if (searchFilters.size() == 2) {
            films = findFilmsByTitleOrDirectorName(query);
        } else {
            throw new BadRequestException("This number of search filters is not supported");
        }
        return mapFilmsToFilmsDTO(likesService.sortFilmsByLikesCount(films));
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

            Mpa mpa = mpaService.findMpaById(film.getMpaId());

            List<Long> genreIds = filmDTO.getGenres().stream().map(GenreDTO::getId).toList();
            List<Long> directorIds = filmDTO.getDirectors().stream().map(DirectorDTO::getId).toList();
            Long addedFilmId = filmRepository.addFilm(film, genreIds, directorIds);

            filmDTO.setId(addedFilmId);
            filmDTO.setMpa(MpaMapper.mapToMpaDTO(mpa));
            return filmDTO;
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

            Mpa mpa = mpaService.findMpaById(film.getMpaId());
            filmDTO.setMpa(MpaMapper.mapToMpaDTO(mpa));
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        List<Long> genreIds = filmDTO.getGenres().stream().map(GenreDTO::getId).toList();
        List<Long> directorIds = filmDTO.getDirectors().stream().map(DirectorDTO::getId).toList();
        if (filmRepository.updateFilm(film, genreIds, directorIds) == 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", filmDTO.getId()));
        }

        return filmDTO;
    }

    public Collection<FilmDTO> getAllFilms() {
        Collection<Film> films = filmRepository.getAllFilms();
        return mapFilmsToFilmsDTO(films);
    }

    public FilmDTO findFilm(Long id) {
        Optional<Film> filmOpt = filmRepository.findFilm(id);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();

            Mpa mpa = mpaService.findMpaById(film.getMpaId());

            List<Genre> genres = genreService.getGenresByFilmId(id);
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
        directorService.findDirectorById(directorId);
        Collection<Film> films = filmRepository.findFilmsByDirector(directorId, sortBy);
        return mapFilmsToFilmsDTO(films);
    }

    public List<FilmDTO> getCommonFilms(Long id, Long friendId) {
        if (userService.findUser(id) == null || userService.findUser(friendId) == null) {
            throw new NotFoundException("User is not found");
        }
        Collection<Film> films = filmRepository.getAllByIds(likesService.getCommonFilms(id, friendId));
        return new ArrayList<>(mapFilmsToFilmsDTO(films));
    }

    public List<FilmDTO> getRecommendation(Long userId) {
        userService.findUser(userId);
        List<Long> recommendedFilmIds = likesService.getRecommendedFilmIds(userId);
        Collection<Film> films = filmRepository.getAllByIds(recommendedFilmIds);
        return new ArrayList<>(mapFilmsToFilmsDTO(films));
    }

    private Collection<FilmDTO> mapFilmsToFilmsDTO(Collection<Film> films) {
        if (films.isEmpty()) {
            return List.of();
        }
        List<Long> filmIds = films.stream().map(Film::getId).toList();
        Map<Long, Mpa> filmsMpa = mpaService.findAllByManyFilmIds(filmIds);
        Map<Long, List<Genre>> filmsGenres = genreService.findAllByManyFilmIds(filmIds);
        Map<Long, List<Director>> filmsDirectors = directorService.findAllByManyFilmIds(filmIds);

        return films.stream().map(film -> {
            Mpa mpa = filmsMpa.get(film.getId());
            List<Genre> genres = filmsGenres.getOrDefault(film.getId(), new ArrayList<>());
            List<Director> directors = filmsDirectors.getOrDefault(film.getId(), new ArrayList<>());
            return FilmMapper.mapToFilmDTO(film, genres, directors, mpa);
        }).toList();
    }

    private Collection<Film> findFilmsByTitle(String titleQuery) {
        return filmRepository.findFilmsByTitle(titleQuery);
    }

    private Collection<Film> findFilmsByDirectorName(String directorNameQuery) {
        return filmRepository.findFilmsByDirectorName(directorNameQuery);
    }

    private Collection<Film> findFilmsByTitleOrDirectorName(String titleOrDirectorQuery) {
        return Stream.concat(
                        findFilmsByTitle(titleOrDirectorQuery).stream(),
                        findFilmsByDirectorName(titleOrDirectorQuery).stream()
                ).collect(Collectors.toSet()
        );
    }
}
