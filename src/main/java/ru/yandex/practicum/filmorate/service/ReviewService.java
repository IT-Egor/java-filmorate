package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeService reviewLikeService;

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        try {
            Review review = ReviewMapper.mapReviewDTOToReview(reviewDTO);
            checkFilmIdAndUserId(review);
            Long addedReviewId = reviewRepository.addReview(review);
            return findReview(addedReviewId);
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
        }

        return findReview(reviewDTO.getReviewId());
    }

    public void deleteReview(Long id) {
        findReview(id);
        if (!reviewRepository.deleteReview(id)) {
            throw new BadRequestException(String.format("Review with id=%s already deleted", id));
        }
    }

    public ReviewDTO findReview(Long id) {
        Optional<Review> reviewOpt = reviewRepository.findReview(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            Integer useful = reviewLikeService.getUseful(id);
            return ReviewMapper.mapToReviewDTO(review, useful);
        } else {
            throw new NotFoundException("Review with id " + id + " not found");
        }
    }

    public Collection<ReviewDTO> getReviewsOfFilm(Long filmId, int count) {

        if (filmId == 0) {
            return reviewRepository.getAllReviews().stream()
                    .map((Review review) -> {
                        Integer useful = reviewLikeService.getUseful(review.getId());
                        return ReviewMapper.mapToReviewDTO(review, useful);
                    }).sorted(Comparator.comparing(ReviewDTO::getUseful).reversed()).toList();
        }
        if (filmId > 0) {
            return reviewRepository.getReviewsOfFilm(filmId, count).stream()
                    .map((Review review) -> {
                        Integer useful = reviewLikeService.getUseful(review.getId());
                        return ReviewMapper.mapToReviewDTO(review, useful);
                    }).sorted(Comparator.comparing(ReviewDTO::getUseful).reversed()).toList();
        }
        return null;
    }

    private void checkFilmIdAndUserId(Review review) {
        if (review.getUserId() < 0) {
            throw new NotFoundException(String.format("User with id=%s not found", review.getUserId()));
        }
        if (review.getFilmId() < 0) {
            throw new NotFoundException(String.format("Film with id=%s not found", review.getUserId()));
        }
    }

}
