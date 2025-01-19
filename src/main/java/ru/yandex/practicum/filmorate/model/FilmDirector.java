package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"filmId", "directorId"})
public class FilmDirector {
    private Long id;
    private Long filmId;
    private Long directorId;
}