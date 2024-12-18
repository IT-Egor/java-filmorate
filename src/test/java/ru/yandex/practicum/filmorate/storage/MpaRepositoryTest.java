package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaRepository.class, MpaRowMapper.class})
class MpaRepositoryTest {
    private final MpaRepository mpaRepository;

    @Test
    void findById() {
        Mpa mpa = new Mpa(1L, "G");
        assertEquals(mpa, mpaRepository.findById(1L).get());
    }

    @Test
    void findAll() {
        List<Mpa> expected = List.of(
                new Mpa(1L, "G"),
                new Mpa(2L, "PG"),
                new Mpa(3L, "PG-13"),
                new Mpa(4L, "R"),
                new Mpa(5L, "NC-17"));
        assertArrayEquals(
                expected.toArray(),
                mpaRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Mpa::getId))
                        .toArray());
    }
}