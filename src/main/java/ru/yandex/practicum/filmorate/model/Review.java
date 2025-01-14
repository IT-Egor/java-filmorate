package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"content", "isPositive"})
public class Review {
    private long id;
    private String content;
    private boolean isPositive;
    private long filmId;
    private long userId;
}
