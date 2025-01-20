package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static class PopularFilmsSqlTemplates {
        public static final String NO_FILTERS = """
                SELECT f.*
                FROM films f
                LEFT JOIN likes l ON f.id = l.film_id
                GROUP BY f.id
                ORDER BY COUNT(l.id) DESC
                LIMIT ?
                """;

        public static final String YEAR_FILTER = """
                SELECT f.*
                FROM films f
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE EXTRACT(YEAR FROM f.release_date) = ?
                GROUP BY f.id
                ORDER BY COUNT(l.id) DESC
                LIMIT ?
                """;

        public static final String GENRE_FILTER = """
                SELECT f.*
                FROM films f
                INNER JOIN film_genres fg ON f.id = fg.film_id
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE fg.genre_id = ?
                GROUP BY f.id
                ORDER BY COUNT(l.id) DESC
                LIMIT ?
                """;

        public static final String YEAR_GENRE_FILTER = """
                SELECT f.*
                FROM films f
                INNER JOIN film_genres fg ON f.id = fg.film_id
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE fg.genre_id = ? AND EXTRACT(YEAR FROM f.release_date) = ?
                GROUP BY f.id
                ORDER BY COUNT(l.id) DESC
                LIMIT ?
                """;
    }

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> getAllFilms() {
        String findAllQuery = "SELECT * FROM films";
        return findMany(findAllQuery);
    }

    public Optional<Film> findFilm(long id) {
        String findByIdQuery = "SELECT * FROM films WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    public Collection<Film> findFilmsByTitle(String titleQuery) {
        String findFilmByTitle = """
            SELECT f.*
            FROM films f
            WHERE f.name iLIKE ?
        """;
        return findMany(findFilmByTitle, String.format("%%%s%%", titleQuery));
    }

    public Collection<Film> findFilmsByDirectorName(String directorNameQuery) {
        String findFilmByTitle = """
            SELECT DISTINCT f.*
            FROM films f
            INNER JOIN film_directors fd ON fd.film_id = f.id
            INNER JOIN directors d ON d.id = fd.director_id
            WHERE d.name iLIKE ?
        """;
        return findMany(findFilmByTitle, String.format("%%%s%%", directorNameQuery));
    }

    public Collection<Film> findFilmsByDirector(Long directorId, String sortBy) {
        String sort = sortBy.equals("year") ? "release_date;" : "likes_count DESC;";
        String query = "SELECT f.*, COUNT(likes.id) as likes_count " +
                "FROM film_directors " +
                "INNER JOIN films AS f ON f.id = film_directors.film_id " +
                "LEFT JOIN likes ON f.id=likes.film_id " +
                "WHERE film_directors.director_id = ? " +
                "GROUP BY f.id, f.name, f.description, f.release_date, f.duration " +
                "ORDER BY ";
        return findMany(query + sort, directorId);
    }

    public Long addFilm(Film film) {
        String insertQuery = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        return insert(insertQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpaId());
    }

    public Long updateFilm(Film film) {
        String updateQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        return update(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpaId(),
                film.getId());
    }

    public void removeFilm(Long id) {
        String deleteFilmQuery = "DELETE from films where id = ?";
        update(deleteFilmQuery, id);
    }

    public Collection<Film> getPopularFilms(Map<String, String> searchFilters) {
        if (searchFilters.size() == 1) {
            return findMany(PopularFilmsSqlTemplates.NO_FILTERS, searchFilters.get("count"));

        } else if (searchFilters.containsKey("year")
                && !searchFilters.containsKey("genreId")
                && searchFilters.size() == 2) {
            return findMany(PopularFilmsSqlTemplates.YEAR_FILTER,
                    searchFilters.get("year"),
                    searchFilters.get("count"));

        } else if (searchFilters.containsKey("genreId")
                && !searchFilters.containsKey("year")
                && searchFilters.size() == 2) {
            return findMany(PopularFilmsSqlTemplates.GENRE_FILTER,
                    searchFilters.get("genreId"),
                    searchFilters.get("count"));

        } else if (searchFilters.containsKey("genreId")
                && searchFilters.containsKey("year")
                && searchFilters.size() == 3) {
            return findMany(PopularFilmsSqlTemplates.YEAR_GENRE_FILTER,
                    searchFilters.get("genreId"),
                    searchFilters.get("year"),
                    searchFilters.get("count"));

        } else {
            throw new BadRequestException("Invalid popular films filters");
        }
    }
}
