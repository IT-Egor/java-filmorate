package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventRepository;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Collection<Event> getFeed(Long userId) {
        Optional<User> userOpt = userRepository.findUser(userId);
        if (userOpt.isPresent()) {
            return eventRepository.getFeed(userId);
        } else {
            throw new NotFoundException(String.format("User with id=%s not found", userId));
        }
    }

    public Long createEvent(Long userId, EventType eventType, EventOperation eventOperation, Long entityId) {
        Event event = new Event();
        event.setTimestamp(Instant.now().toEpochMilli());
        event.setUserId(userId);
        event.setEventType(eventType);
        event.setOperation(eventOperation);
        event.setEntityId(entityId);

        return eventRepository.createEvent(event);
    }
}