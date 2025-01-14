package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDTO {
    @NotNull(message = "Review id is required")
    private long reviewId;

    @NotBlank(message = "Review is required")
    private String content;

    @NotNull(message = "Review type is required")
    private Boolean isPositive;

    @NotNull(message = "User id is required")
    private long userId;

    @NotNull(message = "Film id is required")
    private long filmId;

    private Integer useful = 0;
}
