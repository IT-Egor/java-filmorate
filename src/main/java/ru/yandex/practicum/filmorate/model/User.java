package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    long id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;

    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.login = user.login;
        this.name = user.name;
        this.birthday = user.birthday;
    }
}
