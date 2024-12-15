package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreDTO findGenreDTOById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found"));
        return GenreMapper.mapToGenreDTO(genre);
    }

    public Collection<GenreDTO> getAllGenreDTOs() {
        return genreRepository.findAll().stream().map(GenreMapper::mapToGenreDTO).sorted(Comparator.comparing(GenreDTO::getId)).toList();
    }

    public Genre findGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found"));
    }

    public List<GenreDTO> fixIfNullOrWithDuplicates(List<GenreDTO> genreDTOs) {
        if (genreDTOs == null) {
            return new ArrayList<>();
        } else {
            return genreDTOs.stream().distinct().toList();
        }
    }
}
