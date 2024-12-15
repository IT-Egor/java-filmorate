package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LikesService {
    private final LikeRepository likeRepository;

    public Long addLike(Long userId, Long filmId) {
        return likeRepository.addLikeToFilm(userId, filmId);
    }

    public boolean removeLike(Long userId, Long filmId) {
        return likeRepository.removeLikeFromFilm(userId, filmId);
    }

    public List<LikeDTO> getFilmLikes(Long filmId) {
        return likeRepository.getFilmLikes(filmId).stream().map(LikeMapper::mapToLikeDTO).toList();
    }

    public Map<Long, Long> getFilmsLikesCount() {
        Collection<Like> likes = likeRepository.getAllFilmsLikes();
        return likes.stream()
                .collect(Collectors.groupingBy(
                        Like::getFilmId,
                        Collectors.counting()
                ));
    }
}
