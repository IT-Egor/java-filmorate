package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.service.ReviewLikeService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;


    @PostMapping
    public ReviewDTO createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        return reviewService.saveReview(reviewDTO);
    }

    @PutMapping
    public ReviewDTO updateReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public ReviewDTO getReview(@PathVariable Long id) {
        return reviewService.findReview(id);
    }

    @GetMapping
    public Collection<ReviewDTO> getReviewsOfFilm(
            @RequestParam(required = false, defaultValue = "0", value = "filmId") long filmId,
            @RequestParam(required = false, defaultValue = "10", value = "count") int count) {
        return reviewService.getReviewsOfFilm(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public long addLikeOnReview(@PathVariable Long id, @PathVariable Long userId) {
        return reviewLikeService.addLikeOnReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public long addDislikeOnReview(@PathVariable Long id, @PathVariable Long userId) {
        return reviewLikeService.addDislikeOnReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public long removeLikeOnReview(@PathVariable Long id, @PathVariable Long userId) {
        return reviewLikeService.removeLikeOnReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public long removeDislikeOnReview(@PathVariable Long id, @PathVariable Long userId) {
        return reviewLikeService.removeDislikeOnReview(id, userId);
    }
}