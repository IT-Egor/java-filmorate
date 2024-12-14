package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static FilmDTO mapToFilmDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(MpaMapper.mapToFilmMpaDTO(film.getMpa()));
        dto.setGenres(film.getGenres().stream().map(GenreMapper::mapToGenreDTO).collect(Collectors.toSet()));
        return dto;
    }

    public static Film mapToFilm(FilmDTO dto) {
        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setMpa(MpaMapper.mapFilmMpaDTOToMpa(dto.getMpa()));
        film.setGenres(dto.getGenres().stream().map(GenreMapper::mapGenreDTOToGenre).collect(Collectors.toSet()));
        return film;
    }
}
