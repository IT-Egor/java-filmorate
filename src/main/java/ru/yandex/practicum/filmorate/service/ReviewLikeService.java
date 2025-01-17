package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.ReviewLike;
import ru.yandex.practicum.filmorate.storage.ReviewLikeRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewService reviewService;

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
            reviewService.updateUsefulOfReview(reviewId, useful);
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
        reviewService.updateUsefulOfReview(id, useful);
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
}


