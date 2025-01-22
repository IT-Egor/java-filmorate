package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreRepository;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final FilmGenreRepository filmGenreRepository;

    public GenreDTO findGenreDTOById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Genre with id %s not found", id)));
        return GenreMapper.mapToGenreDTO(genre);
    }

    public Collection<GenreDTO> getAllGenreDTOs() {
        return genreRepository.findAll().stream().map(GenreMapper::mapToGenreDTO).sorted(Comparator.comparing(GenreDTO::getId)).toList();
    }

    public Genre findGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Genre with id %s not found", id)));
    }

    public List<GenreDTO> fixIfNullOrWithDuplicates(List<GenreDTO> genreDTOs) {
        if (genreDTOs == null) {
            return new ArrayList<>();
        } else {
            return genreDTOs.stream().distinct().map(genreDTO -> findGenreDTOById(genreDTO.getId())).toList();
        }
    }

    public void addGenresToFilm(List<Long> genresIds, Long filmId) {
        filmGenreRepository.batchInsert(genresIds, filmId);
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        List<FilmGenre> filmGenres = new ArrayList<>(filmGenreRepository.findAllByFilmId(filmId));
        return filmGenres.stream().map(filmGenre ->
                findGenreById(filmGenre.getGenreId())
        ).toList();
    }

    public boolean deleteFilmGenres(Long filmId) {
        return filmGenreRepository.deleteFilmGenres(filmId);
    }
}
