package ru.yandex.practicum.filmorate.dto;

import lombok.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    @NonNull
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private Duration duration;
    private Set<Long> likedUsersIds = new HashSet<>();

    public void addLike(User user) {
        likedUsersIds.add(user.getId());
    }

    public void removeLike(User user) {
        likedUsersIds.remove(user.getId());
    }

    public long getLikesCount() {
        return likedUsersIds.size();
    }
}
