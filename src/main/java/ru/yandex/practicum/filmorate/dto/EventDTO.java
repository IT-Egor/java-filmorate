package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

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
