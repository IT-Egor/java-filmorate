package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDTO {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private MpaDTO mpa;
    private Set<GenreDTO> genres;
}
