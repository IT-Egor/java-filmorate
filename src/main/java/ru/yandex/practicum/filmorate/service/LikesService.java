package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.impl.db.LikeDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LikesService {
    private final LikeDbStorage likeDbStorage;

    public Long addLike(Long userId, Long filmId) {
        return likeDbStorage.addLikeToFilm(userId, filmId);
    }

    public boolean removeLike(Long userId, Long filmId) {
        return likeDbStorage.removeLikeFromFilm(userId, filmId);
    }

    public List<Like> getFilmLikes(Long filmId) {
        return new ArrayList<>(likeDbStorage.getFilmLikes(filmId));
    }
}
