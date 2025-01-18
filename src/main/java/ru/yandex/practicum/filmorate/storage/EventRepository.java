package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.util.Collection;

@Repository
public class EventRepository extends BaseRepository<Event> {
    public EventRepository(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Event> getFeed(Long userId) {
        String getFeedQuery = "SELECT * " +
                              "FROM feed " +
                              "WHERE user_id = ? ";
        return findMany(getFeedQuery, userId);
    }

    public Long createEvent(Event event) {
        String createEventQuery = "INSERT INTO feed (user_id, timestamp, event_type_name, event_operation_name, entity_id) values (?, ?, ?, ?, ?)";

        String operationId = getOperationId(event.getOperation());
        String entityTypeId = getEventTypeId(event.getEventType());

        return insert(
                createEventQuery,
                event.getUserId(),
                event.getTimestamp(),
                entityTypeId,
                operationId,
                event.getEntityId());
    }

    private String getOperationId(EventOperation eventOperation) {
        return switch (eventOperation) {
            case REMOVE -> "REMOVE";
            case ADD -> "ADD";
            case UPDATE -> "UPDATE";
            default -> throw new IllegalArgumentException("Unknown operation type: " + eventOperation);
        };
    }

    private String getEventTypeId(EventType eventType) {
        return switch (eventType) {
            case LIKE -> "LIKE";
            case REVIEW -> "REVIEW";
            case FRIEND -> "FRIEND";
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}