package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<Long> friends;
}
