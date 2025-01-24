package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MergeUserRequest {
    private long id;

    @Email(message = "Invalid email")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Login is required")
    @NotBlank(message = "Invalid login")
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
