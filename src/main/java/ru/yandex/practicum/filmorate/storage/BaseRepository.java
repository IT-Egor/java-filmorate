package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class BaseRepository<T> {
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

    protected boolean delete(String deleteQuery, Object... params) {
        int rowsDeleted = jdbc.update(deleteQuery, params);
        return rowsDeleted > 0;
    }

    protected long update(String updateQuery, Object... params) {
        return jdbc.update(updateQuery, params);
    }

    protected long insert(String insertQuery, Object... params) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
                    ps.setObject(paramIndex + 1, params[paramIndex]);
                }
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKeyAs(Long.class);

            if (id != null) {
                return id;
            } else {
                throw new InternalServerException("Failed to insert data");
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation");
        }
    }

    protected void connectingTablesBatchInsert(String tableName,
                                               String objectIdColumnName,
                                               String targetIdColumnName,
                                               List<Long> objectIds,
                                               long targetId) {
        try {
            String insert = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", tableName, targetIdColumnName, objectIdColumnName);
            jdbc.batchUpdate(insert, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, targetId);
                    ps.setLong(2, objectIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return objectIds.size();
                }
            });
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation");
        }
    }
}
