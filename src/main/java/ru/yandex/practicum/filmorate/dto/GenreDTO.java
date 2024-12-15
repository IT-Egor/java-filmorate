package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GenreDTO {
    @NotNull(message = "Genre id is required")
    @Positive(message = "Genre id must be positive")
    private Long id;
    private String name;
}