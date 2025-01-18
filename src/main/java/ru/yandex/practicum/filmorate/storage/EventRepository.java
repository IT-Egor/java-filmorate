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
        String getFeedQuery = """
                    SELECT
                      f.event_id,
                      f.user_id,
                      f.timestamp,
                      et.name AS event_type,
                      op.name AS operation,
                      f.entity_id
                      FROM
                      feed f
                    JOIN event_type et ON f.event_type_id = et.id
                    JOIN event_operation op ON f.event_operation_id = op.id
                    WHERE
                      f.user_id = ?
                """;
        return findMany(getFeedQuery, userId);
    }

    public Long createEvent(Event event) {
        String createEventQuery = "INSERT INTO feed (user_id, timestamp, event_type_id, event_operation_id, entity_id) values (?, ?, ?, ?, ?)";

        Long operationId = getOperationId(event.getOperation());
        Long entityTypeId = getEventTypeId(event.getEventType());

        return insert(
                createEventQuery,
                event.getUserId(),
                event.getTimestamp(),
                entityTypeId,
                operationId,
                event.getEntityId());
    }

    private Long getOperationId(EventOperation eventOperation) {
        return switch (eventOperation) {
            case REMOVE -> 1L;
            case ADD -> 2L;
            case UPDATE -> 3L;
            default -> throw new IllegalArgumentException("Unknown operation type: " + eventOperation);
        };
    }

    private Long getEventTypeId(EventType eventType) {
        return switch (eventType) {
            case LIKE -> 1L;
            case REVIEW -> 2L;
            case FRIEND -> 3L;
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}