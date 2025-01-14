package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
public class ReviewRepository extends BaseRepository<Review> {

    public ReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Long addReview(Review review) {
        String insertQuery = "INSERT INTO reviews (film_id, user_id, content, isPositive) VALUES (?, ?, ?, ?)";
        return insert(insertQuery,
                review.getFilmId(),
                review.getUserId(),
                review.getContent(),
                review.isPositive());
    }

    public Long updateReview(Review review) {
        String updateQuery = "UPDATE reviews SET film_id = ?, user_id = ?, content = ?, isPositive = ? WHERE id = ?";
        return update(updateQuery,
                review.getFilmId(),
                review.getUserId(),
                review.getContent(),
                review.isPositive(),
                review.getId());
    }

    public boolean deleteReview(Long id) {
        String delete = "DELETE FROM reviews WHERE id = ?";
        return delete(delete, id);
    }

    public Optional<Review> findReview(long id) {
        String findByIdQuery = "SELECT * FROM reviews WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    public Collection<Review> getAllReviews() {
        String findAllQuery = "SELECT * FROM reviews";
        return findMany(findAllQuery);
    }

    public Collection<Review> getReviewsOfFilm(Long filmId, int count) {
        String findAllQuery = "SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
        return findMany(findAllQuery, filmId, count);
    }
}
