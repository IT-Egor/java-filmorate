package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findById(Long id) {
        String selectOne = "SELECT * FROM ratings WHERE id = ?";
        return findOne(selectOne, id);
    }

    public Collection<Mpa> findAll() {
        String selectAll = "SELECT * FROM ratings";
        return findMany(selectAll);
    }

    public Map<Long, Mpa> findAllByManyFilmIds(Collection<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String selectAllByManyIds = String.format("""
                        SELECT f.id film_id, r.id rating_id, r.name rating_name
                        FROM films f
                        INNER JOIN ratings r ON f.rating_id = r.id
                        WHERE f.id IN (%s)
                        """, inSql);

        Map<Long, Mpa> filmsMpa = new HashMap<>();
        SqlRowSet rowSet = jdbc.queryForRowSet(selectAllByManyIds, filmIds.toArray());
        while (rowSet.next()) {
            Mpa mpa = new Mpa(rowSet.getLong("RATING_ID"), rowSet.getString("RATING_NAME"));
            filmsMpa.put(rowSet.getLong("FILM_ID"), mpa);
        }
        return filmsMpa;
    }
}
