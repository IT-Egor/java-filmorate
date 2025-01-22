package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> findById(Long id) {
        String selectById = "SELECT * FROM genres WHERE id = ?";
        return findOne(selectById, id);
    }

    public Collection<Genre> findAll() {
        String selectAll = "SELECT * FROM genres";
        return findMany(selectAll);
    }

    public Collection<Genre> getGenresByFilmId(Long filmId) {
        String selectAllByFilmId = """
                SELECT g.*
                FROM genres g
                INNER JOIN film_genres fg ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;
        return findMany(selectAllByFilmId, filmId);
    }

    public Map<Long, List<Genre>> findAllByManyFilmIds(Collection<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String selectAllByManyIds = String.format("""
                        SELECT f.id film_id, g.id genre_id, g.name genre_name
                        FROM films f
                        INNER JOIN film_genres fg ON f.id = fg.film_id
                        INNER JOIN genres g ON fg.genre_id = g.id
                        WHERE f.id IN (%s)
                        """, inSql);

        Map<Long, List<Genre>> filmsGenres = new HashMap<>();
        SqlRowSet rowSet = jdbc.queryForRowSet(selectAllByManyIds, filmIds.toArray());
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getLong("GENRE_ID"), rowSet.getString("GENRE_NAME"));
            filmsGenres.putIfAbsent(rowSet.getLong("FILM_ID"), new ArrayList<>());
            filmsGenres.get(rowSet.getLong("FILM_ID")).add(genre);
        }
        return filmsGenres;
    }
}
