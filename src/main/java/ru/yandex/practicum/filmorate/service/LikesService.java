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

    public Long getFilmLikesCount(Long filmId) {
        return likeRepository.getFilmLikesCount(filmId);
    }

    public List<Film> sortFilmsByLikesCount(Collection<Film> filmsToSort) {
        ArrayList<Film> sortedFilms = new ArrayList<>(filmsToSort);
        Map<Long, Long> filmLikesCount = new HashMap<>();
        sortedFilms.forEach(film -> filmLikesCount.put(film.getId(), getFilmLikesCount(film.getId())));

        sortedFilms.sort(Comparator.comparing(
                (Film film) -> filmLikesCount.get(film.getId())
        ).reversed());
        return sortedFilms;
    }

    public List<Long> getMostLikedFilms(int selectionLimit) {
        return likeRepository.getMostLikedFilmsIds(selectionLimit);
    }
}
