package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.*;

@Repository
public class DirectorRepository extends BaseRepository<Director> {
    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Director> getAllDirectors() {
        String selectAll = "SELECT * FROM directors";
        return findMany(selectAll);
    }

    public Optional<Director> findById(Long id) {
        String selectById = "SELECT * FROM directors WHERE id = ?";
        return findOne(selectById, id);
    }

    public Long addDirector(Director director) {
        String insert = "INSERT INTO directors (name) VALUES (?)";
        return insert(insert, director.getName());
    }

    public Long updateDirector(Director director) {
        String update = "UPDATE directors SET name = ? WHERE id = ?";
        return update(update, director.getName(), director.getId());
    }

    public void removeDirector(Long directorId) {
        String delete = "DELETE FROM directors WHERE id = ?";
        delete(delete, directorId);
    }

    public Collection<Director> findAllByFilmId(Long filmId) {
        String selectAllByFilmId = """
                SELECT d.*
                FROM directors d
                INNER JOIN film_directors fd ON d.id = fd.director_id
                WHERE fd.film_id = ?
                """;
        return findMany(selectAllByFilmId, filmId);
    }

    public Map<Long, List<Director>> findAllByManyFilmIds(Collection<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String selectAllByManyIds = String.format("""
                        SELECT f.id film_id, d.id director_id, d.name director_name
                        FROM films f
                        INNER JOIN film_directors fd ON f.id = fd.film_id
                        INNER JOIN directors d ON fd.director_id = d.id
                        WHERE f.id IN (%s)
                        """, inSql);

        Map<Long, List<Director>> filmsDirectors = new HashMap<>();
        SqlRowSet rowSet = jdbc.queryForRowSet(selectAllByManyIds, filmIds.toArray());
        while (rowSet.next()) {
            Director director = new Director(rowSet.getLong("DIRECTOR_ID"), rowSet.getString("DIRECTOR_NAME"));
            filmsDirectors.putIfAbsent(rowSet.getLong("FILM_ID"), new ArrayList<>());
            filmsDirectors.get(rowSet.getLong("FILM_ID")).add(director);
        }
        return filmsDirectors;
    }
}