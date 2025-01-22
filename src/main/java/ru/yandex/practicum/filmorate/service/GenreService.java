package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreDTO findGenreDTOById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Genre with id %s not found", id)));
        return GenreMapper.mapToGenreDTO(genre);
    }

    public Collection<GenreDTO> getAllGenreDTOs() {
        return genreRepository.findAll().stream().map(GenreMapper::mapToGenreDTO).sorted(Comparator.comparing(GenreDTO::getId)).toList();
    }

    public List<GenreDTO> fixIfNullOrWithDuplicates(List<GenreDTO> genreDTOs) {
        if (genreDTOs == null) {
            return new ArrayList<>();
        } else {
            return genreDTOs.stream().distinct().map(genreDTO -> findGenreDTOById(genreDTO.getId())).toList();
        }
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        return new ArrayList<>(genreRepository.getGenresByFilmId(filmId));
    }

    public Map<Long, List<Genre>> findAllByManyFilmIds(Collection<Long> filmIds) {
        return genreRepository.findAllByManyFilmIds(filmIds);
    }
}
