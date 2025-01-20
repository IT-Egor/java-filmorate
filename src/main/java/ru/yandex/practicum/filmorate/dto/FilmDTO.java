package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.annotations.PositiveDuration;
import ru.yandex.practicum.filmorate.validator.annotations.ReleaseDate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDTO {
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;

    @NotNull(message = "Release date is required")
    @ReleaseDate
    private LocalDate releaseDate;

    @PositiveDuration
    private Duration duration;

    @Valid
    @NotNull(message = "MPA is required")
    private MpaDTO mpa;

    @Valid
    private List<GenreDTO> genres;

    private List<DirectorDTO> directors;
}
