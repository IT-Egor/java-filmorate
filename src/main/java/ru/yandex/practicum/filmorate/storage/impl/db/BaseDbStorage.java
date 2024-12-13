package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public abstract class BaseDbStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String selectQuery, Object... params) {
        try {
            T result = jdbc.queryForObject(selectQuery, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected Collection<T> findMany(String selectQuery, Object... params) {
        return jdbc.query(selectQuery, mapper, params);
    }

    protected boolean delete(String deleteQuery, long id) {
        int rowsDeleted = jdbc.update(deleteQuery, id);
        return rowsDeleted > 0;
    }

    protected long merge(String mergeQuery, Object... params) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(mergeQuery, Statement.RETURN_GENERATED_KEYS);
                for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
                    ps.setObject(paramIndex + 1, params[paramIndex]);
                }
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKeyAs(Long.class);

            if (id != null) {
                return id;
            } else {
                int methodBeginIndex = 0;
                int methodEndIndex = 6;
                throw new InternalServerException(String.format("Failed to %s data",
                        mergeQuery.substring(methodBeginIndex, methodEndIndex).toLowerCase()));
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation");
        }
    }
}
