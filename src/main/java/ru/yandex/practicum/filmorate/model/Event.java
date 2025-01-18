package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private long eventId;
    private long timestamp;
    private long userId;
    private EventType eventType;
    private EventOperation operation;
    private long entityId;
}