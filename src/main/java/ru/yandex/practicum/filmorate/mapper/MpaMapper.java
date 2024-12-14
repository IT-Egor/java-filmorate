package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmMpaDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static FilmMpaDTO mapToFilmMpaDTO(Mpa mpa) {
        FilmMpaDTO dto = new FilmMpaDTO();
        dto.setId(mpa.getId());
        dto.setName(mpa.getName());
        return dto;
    }

    public static Mpa mapFilmMpaDTOToMpa(FilmMpaDTO dto) {
        Mpa mpa = new Mpa();
        mpa.setId(dto.getId());
        mpa.setName(dto.getName());
        return mpa;
    }
}