package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;

    public GenreDTO findGenreDTOById(Long id) {
        Genre genre = genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found"));
        return GenreMapper.mapToGenreDTO(genre);
    }

    public Genre findGenreById(Long id) {
        return genreStorage.findById(id)
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
