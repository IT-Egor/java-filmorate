package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.model.Review;

public class ReviewMapper {
    public static ReviewDTO mapToReviewDTO(Review review, Integer useful) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getId());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setIsPositive(review.isPositive());
        reviewDTO.setUserId(review.getUserId());
        reviewDTO.setFilmId(review.getFilmId());
        reviewDTO.setUseful(useful);
        return reviewDTO;
    }

    public static Review mapReviewDTOToReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getReviewId());
        review.setContent(reviewDTO.getContent());
        review.setPositive(reviewDTO.getIsPositive());
        review.setFilmId(reviewDTO.getFilmId());
        review.setUserId(reviewDTO.getUserId());
        return review;
    }
}
