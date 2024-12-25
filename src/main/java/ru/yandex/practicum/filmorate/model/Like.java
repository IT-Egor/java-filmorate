package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"filmId", "userId"})
public class Like {
    private Long id;
    private Long filmId;
    private Long userId;
}
