package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmGenreRowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmGenreRepository.class, FilmGenreRowMapper.class})
class FilmGenreRepositoryTest {
    private final FilmGenreRepository filmGenreRepository;

    @Test
    void batchInsert() {
        List<FilmGenre> expected = List.of(
                new FilmGenre(4L, 3L, 1L),
                new FilmGenre(5L, 3L, 2L),
                new FilmGenre(6L, 3L, 3L)
        );
        List<Long> genresIds = List.of(1L, 2L, 3L);
        filmGenreRepository.batchInsert(genresIds, 3);

        List<FilmGenre> actual = new ArrayList<>(filmGenreRepository.findAllByFilmId(3L));
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void findAllByFilmId() {
        List<FilmGenre> expected = List.of(
                new FilmGenre(1L, 1L, 1L),
                new FilmGenre(2L, 1L, 2L)
        );
        assertArrayEquals(expected.toArray(), filmGenreRepository.findAllByFilmId(1L).toArray());
    }

    @Test
    void deleteFilmGenres() {
        filmGenreRepository.deleteFilmGenres(2L);
        assertTrue(filmGenreRepository.findAllByFilmId(2L).isEmpty());
    }
}