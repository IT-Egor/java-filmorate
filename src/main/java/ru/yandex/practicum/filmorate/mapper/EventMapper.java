package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static EventDTO mapToEventDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setEventId(dto.getEventId());
        dto.setTimestamp(event.getTimestamp());
        dto.setUserId(event.getUserId());
        dto.setEventType(event.getEventType());
        dto.setOperation(event.getOperation());
        dto.setEntityId(event.getEntityId());
        return dto;
    }

    public static Event mapToEvent(EventDTO dto) {
        Event event = new Event();
        event.setEventId(dto.getEventId());
        event.setTimestamp(dto.getTimestamp());
        event.setUserId(dto.getUserId());
        event.setEventType(dto.getEventType());
        event.setOperation(dto.getOperation());
        event.setEntityId(dto.getEntityId());
        return event;
    }
}
