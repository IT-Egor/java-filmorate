package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;
import ru.yandex.practicum.filmorate.storage.ReviewLikeRepository;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final EventService eventService;

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        try {
            Review review = ReviewMapper.mapReviewDTOToReview(reviewDTO);
            checkFilmIdAndUserId(review);

            Long addedReviewId = reviewRepository.addReview(review);
            eventService.createEvent(review.getUserId(), EventType.REVIEW, EventOperation.ADD, addedReviewId);

            reviewDTO.setReviewId(addedReviewId);
            return reviewDTO;
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Review review;

        try {
            review = ReviewMapper.mapReviewDTOToReview(reviewDTO);
            checkFilmIdAndUserId(review);
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        if (reviewRepository.updateReview(review) == 0) {
            throw new NotFoundException(String.format("Review with id=%s not found", reviewDTO.getReviewId()));
        } else {
            ReviewDTO oldReview = findReview(reviewDTO.getReviewId());
            reviewDTO.setUserId(oldReview.getUserId());
            reviewDTO.setFilmId(oldReview.getFilmId());
            reviewDTO.setReviewId(oldReview.getReviewId());
            eventService.createEvent(reviewDTO.getUserId(), EventType.REVIEW, EventOperation.UPDATE, reviewDTO.getReviewId());
        }
        return reviewDTO;
    }

    public void deleteReview(Long id) {
        findReview(id);
        ReviewDTO review = findReview(id);
        if (!reviewRepository.deleteReview(id)) {
            throw new BadRequestException(String.format("Review with id=%s already deleted", id));
        } else {
            eventService.createEvent(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE, id);
        }
    }

    public ReviewDTO findReview(Long id) {
        Optional<Review> reviewOpt = reviewRepository.findReview(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            return ReviewMapper.mapToReviewDTO(review);
        } else {
            throw new NotFoundException("Review with id " + id + " not found");
        }
    }

    public Collection<ReviewDTO> getReviewsOfFilm(Long filmId, int count) {
        if (filmId == 0) {
            return reviewRepository.getAllReviews().stream()
                    .map(ReviewMapper::mapToReviewDTO).toList();
        }

        if (filmId > 0) {
            return reviewRepository.getReviewsOfFilm(filmId, count).stream()
                    .map(ReviewMapper::mapToReviewDTO).toList();
        }
        return Collections.emptyList();
    }

    public void updateUsefulOfReview(Long reviewId, Long useful) {
        if (reviewRepository.updateUsefulOfReview(reviewId, useful) == 0) {
            throw new NotFoundException(String.format("Useful of review with id=%s: update error", reviewId));
        }
    }

    public long addLikeOnReview(Long reviewId, Long userId) {
        Integer likeReview = 1;
        return addLikeOrDislikeOnReview(reviewId, userId, likeReview);
    }

    public long addDislikeOnReview(Long reviewId, Long userId) {
        Integer likeReview = -1;
        return addLikeOrDislikeOnReview(reviewId, userId, likeReview);
    }

    public long removeLikeOnReview(Long id, Long userId) {
        return removeLikeOrDislikeOnReview(id, userId);
    }

    public long removeDislikeOnReview(Long id, Long userId) {
        return removeLikeOrDislikeOnReview(id, userId);
    }

    public Long getUseful(Long reviewId) {
        Optional<Long> usefulOpt = reviewLikeRepository.getUseful(reviewId);
        return usefulOpt.orElse(0L);
    }

    private long addLikeOrDislikeOnReview(Long reviewId, Long userId, Integer likeReview) {
        Optional<ReviewLike> reviewLikeOpt = reviewLikeRepository.findReviewLike(reviewId, userId);
        if (reviewLikeOpt.isPresent()) {
            if (reviewLikeOpt.get().getLike().equals(likeReview)) {
                return reviewLikeOpt.get().getId();
            }
            removeLikeOrDislikeOnReview(reviewId, userId);
        }
        try {
            long likeId = reviewLikeRepository.addLikeOrDislikeOnReview(reviewId, userId, likeReview);
            Long useful = getUseful(reviewId);
            updateUsefulOfReview(reviewId, useful);
            return likeId;
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private long removeLikeOrDislikeOnReview(Long id, Long userId) {
        ReviewLike reviewLike = findReviewLike(id, userId);
        if (!reviewLikeRepository.removeLikeOrDislikeOnReview(reviewLike)) {
            throw new BadRequestException(String.format("Review with id=%s already deleted", id));
        }
        Long useful = getUseful(id);
        updateUsefulOfReview(id, useful);
        return reviewLike.getId();
    }

    private ReviewLike findReviewLike(Long id, Long userId) {
        Optional<ReviewLike> reviewLikeOpt = reviewLikeRepository.findReviewLike(id, userId);

        if (reviewLikeOpt.isPresent()) {
            return reviewLikeOpt.get();
        } else {
            throw new NotFoundException("ReviewLike with id " + id + " not found");
        }
    }

    private void checkFilmIdAndUserId(Review review) {
        if (review.getUserId() < 0) {
            throw new NotFoundException(String.format("User with id=%s not found", review.getUserId()));
        }
        if (review.getFilmId() < 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", review.getUserId()));
        }
        if (review.getUserId() == 0) {
            throw new BadRequestException("User id is null");
        }
        if (review.getFilmId() == 0) {
            throw new BadRequestException("Film id is null");
        }
    }
}
