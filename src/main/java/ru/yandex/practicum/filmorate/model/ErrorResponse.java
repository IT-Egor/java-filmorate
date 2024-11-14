package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String error;
    private final LocalDateTime timestamp;
    private final int status;
}

