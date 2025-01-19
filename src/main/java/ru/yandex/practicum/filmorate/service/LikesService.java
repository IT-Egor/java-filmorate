package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeRepository;
import java.util.*;

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

    public List<Long> getCommonFilms(Long userId, Long friendId) {
        return likeRepository.getCommonFilms(userId, friendId);
    }

    public Long getFilmLikesCount(Long filmId) {
        return likeRepository.getFilmLikesCount(filmId);
    }

    public List<Film> sortFilmsByLikesCount(Collection<Film> filmsToSort) {
        ArrayList<Film> sortedFilms = new ArrayList<>(filmsToSort);
        Map<Long, Long> filmLikesCount = new HashMap<>();
        sortedFilms.forEach(film -> filmLikesCount.put(film.getId(), getFilmLikesCount(film.getId())));

        sortedFilms.sort((Film film1, Film film2) -> {
            if (filmLikesCount.get(film1.getId()) == 0L && filmLikesCount.get(film2.getId()) == 0) {
                return Long.compare(film1.getId(), film2.getId());
            } else {
                return Long.compare(filmLikesCount.get(film2.getId()), filmLikesCount.get(film1.getId()));
            }
        });
        return sortedFilms;
    }
}
