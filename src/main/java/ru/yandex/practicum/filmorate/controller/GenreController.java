package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<GenreDTO> getAll() {
        return genreService.getAllGenreDTOs();
    }

    @GetMapping("/{genreId}")
    public GenreDTO getGenreById(@PathVariable Long genreId) {
        return genreService.findGenreDTOById(genreId);
    }
}
