package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"reviewId", "userId"})
public class ReviewLike {
    private Long id;
    private Long reviewId;
    private Long userId;
    private Integer likeReview;
}
