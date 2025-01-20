package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.storage.LikeRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class LikesService {
    private final LikeRepository likeRepository;

    public void addLike(Long userId, Long filmId) {
        likeRepository.addLikeToFilm(userId, filmId);
    }

    public boolean removeLike(Long userId, Long filmId) {
        return likeRepository.removeLikeFromFilm(userId, filmId);
    }

    public List<LikeDTO> getFilmLikes(Long filmId) {
        return likeRepository.getFilmLikes(filmId).stream().map(LikeMapper::mapToLikeDTO).toList();
    }
}
