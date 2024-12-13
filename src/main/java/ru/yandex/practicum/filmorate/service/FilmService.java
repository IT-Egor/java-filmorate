package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utility.Validator;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {
    private final @Qualifier("filmDbStorage") FilmStorage filmStorage;
    private final UserService userService;

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
        Film film = FilmMapper.mapToFilm(filmDTO);
        Validator.validateFilm(film);
        return FilmMapper.mapToFilmDTO(filmStorage.addFilm(film));
    }

    public FilmDTO updateFilm(Film film) {
        Validator.validateFilm(film);
        return FilmMapper.mapToFilmDTO(filmStorage.updateFilm(film));
    }

    public Collection<FilmDTO> getAllFilms() {
        return filmStorage.getAllFilms().stream().map(FilmMapper::mapToFilmDTO).toList();
    }

    public FilmDTO findFilm(Long id) {
        Optional<Film> filmOpt = filmStorage.findFilm(id);
        if (filmOpt.isPresent()) {
            return FilmMapper.mapToFilmDTO(filmOpt.get());
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }
}
