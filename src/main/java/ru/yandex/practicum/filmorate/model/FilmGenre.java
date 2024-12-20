package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"filmId", "genreId"})
public class FilmGenre {
    private Long id;
    private Long filmId;
    private Long genreId;
}
