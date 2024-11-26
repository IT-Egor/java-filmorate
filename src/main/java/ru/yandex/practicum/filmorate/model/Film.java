package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Film {
    private @NonNull long id;
    private @NonNull String name;
    private @NonNull String description;
    private @NonNull LocalDate releaseDate;
    private @NonNull Duration duration;
    private @NonNull Rating rating;
    private Set<Long> likedUsersIds = new HashSet<>();
    private Set<String> genres = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = Rating.G;
    }

    public void addLike(User user) {
        likedUsersIds.add(user.getId());
    }

    public void removeLike(User user) {
        likedUsersIds.remove(user.getId());
    }

    public long getLikesCount() {
        return likedUsersIds.size();
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void removeGenre(String genre) {
        genres.remove(genre);
    }
}
