package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static MpaDTO mapToDTO(Mpa mpa) {
        MpaDTO dto = new MpaDTO();
        dto.setId(mpa.getId());
        return dto;
    }

    public static Mpa mapToMpa(MpaDTO dto) {
        Mpa mpa = new Mpa();
        mpa.setId(dto.getId());
        return mpa;
    }
}