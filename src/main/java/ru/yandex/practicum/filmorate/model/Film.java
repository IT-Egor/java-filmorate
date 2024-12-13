package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Mpa mpa;
}
