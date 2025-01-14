package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventRepository;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    final private EventRepository eventRepository;
    final private UserRepository userRepository;

    public Collection<Event> getFeed(Long userId) {
        Optional.ofNullable(userRepository.findUser(userId))
                .orElseThrow(() -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        return eventRepository.getFeed(userId);
    }

    public void createEvent(EventDTO eventDTO) {
        try {
            Event event = EventMapper.mapToEvent(eventDTO);
            eventRepository.createEvent(event);

        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}