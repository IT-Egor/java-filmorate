package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"content", "type"})
public class Review {
    private long id;
    private String content;
    private boolean type;
    private long filmId;
    private long userId;
}
