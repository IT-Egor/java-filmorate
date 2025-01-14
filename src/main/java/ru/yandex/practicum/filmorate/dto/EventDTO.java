package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.validator.annotations.PositiveDuration;
import ru.yandex.practicum.filmorate.validator.annotations.ReleaseDate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
public class EventDTO {

    private long eventId;

    @NotNull(message = "Timestamp is required")
    private long timestamp;

    @Valid
    @NotNull(message = "UserId is required")
    private long userId;

    @NotNull(message = "Event Type is required")
    private EventType eventType;

    @NotNull(message = "Event Operation is required")
    private EventOperation operation;

    @NotNull(message = "Target ID is required")
    private long entityId;

}
