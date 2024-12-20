package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId", "friendId"})
public class Friend {
    private Long id;
    private Long userId;
    private Long friendId;
}
