package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

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
        String createEventQuery = "insert into feed (user_id, timestamp, event_type, operation, entity_id) values (?, ?, ?, ?, ?)";

        return insert(
                createEventQuery,
                event.getUserId(),
                event.getTimestamp(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getEntityId());
    }
}