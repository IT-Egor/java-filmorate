package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.Optional;

@Repository
public class ReviewLikeRepository extends BaseRepository<ReviewLike> {
    public ReviewLikeRepository(JdbcTemplate jdbc, RowMapper<ReviewLike> mapper) {
        super(jdbc, mapper);
    }

    public long addLikeOrDislikeOnReview(Long reviewId, Long userId, Integer likeReview) {
        String insertQuery = "INSERT INTO like_reviews (review_id, user_id, like_review) VALUES (?, ?, ?)";
        return insert(insertQuery,
                reviewId,
                userId,
                likeReview);
    }

    public Optional<ReviewLike> findReviewLike(Long reviewId, Long userId) {
        String findByIdQuery = "SELECT * FROM like_reviews WHERE review_id = ? AND user_id = ?";
        return findOne(findByIdQuery, reviewId, userId);
    }

    public Optional<Long> getUseful(Long reviewId) {
        String findByIdQuery = "SELECT SUM(like_review) FROM like_reviews WHERE review_id = ?";

        try {
            Long result = jdbc.queryForObject(findByIdQuery, Long.class, reviewId);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public boolean removeLikeOrDislikeOnReview(ReviewLike reviewLike) {
        String delete = "DELETE FROM like_reviews WHERE id = ?";
        return delete(delete, reviewLike.getId());
    }
}
