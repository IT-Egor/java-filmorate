package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public void batchInsert(List<Long> genreIds, long filmId) {
        connectingTablesBatchInsert("film_genres",
                "genre_id",
                "film_id",
                genreIds,
                filmId);
    }

    public boolean deleteFilmGenres(Long filmId) {
        String deleteFilmGenres = "DELETE FROM film_genres WHERE film_id = ?";
        return delete(deleteFilmGenres, filmId);
    }
}
