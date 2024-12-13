package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class FilmDTO {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private MpaDTO mpa;
}
