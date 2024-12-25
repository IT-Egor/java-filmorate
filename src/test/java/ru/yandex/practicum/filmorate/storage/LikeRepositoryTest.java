package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.mapper.LikeRowMapper;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({LikeRepository.class, LikeRowMapper.class})
class LikeRepositoryTest {
    private final LikeRepository likeRepository;

    @Test
    void getFilmLikes() {
        List<Like> likes = List.of(
                new Like(1L, 1L, 1L),
                new Like(2L, 1L, 2L)
        );
        assertArrayEquals(likes.toArray(), likeRepository.getFilmLikes(1L).toArray());
    }

    @Test
    void addLikeToFilm() {
        likeRepository.addLikeToFilm(1L, 3L);
        assertEquals(3, likeRepository.getFilmLikes(1L).size());
    }

    @Test
    void removeLikeFromFilm() {
        likeRepository.removeLikeFromFilm(2L, 1L);
        assertEquals(0, likeRepository.getFilmLikes(2L).size());
    }

    @Test
    void getMostLikedFilmsIds() {
        assertArrayEquals(new Long[] {3L, 1L, 2L}, likeRepository.getMostLikedFilmsIds(3).toArray());
    }
}