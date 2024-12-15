package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<LikeDTO> getFilmLikes(Long filmId) {
        return likeDbStorage.getFilmLikes(filmId).stream().map(LikeMapper::mapToLikeDTO).toList();
    }

    public Map<Long, Long> getFilmsLikesCount() {
        Collection<Like> likes = likeDbStorage.getAllFilmsLikes();
        return likes.stream()
                .collect(Collectors.groupingBy(
                        Like::getFilmId,
                        Collectors.counting()
                ));
    }
}
