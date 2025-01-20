package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
}