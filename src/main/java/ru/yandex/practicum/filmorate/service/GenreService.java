package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.db.GenreStorage;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreDTO findGenreDTOById(Long id) {
        Genre genre = genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found"));
        return GenreMapper.mapToGenreDTO(genre);
    }

    public Genre findGenreById(Long id) {
        return genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found"));
    }
}