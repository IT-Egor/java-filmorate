package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MpaDTO {
    @NotNull(message = "Mpa id is required")
    @Positive(message = "Mpa id must be positive")
    private Long id;

    private String name;
}
