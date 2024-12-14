package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static MpaDTO mapToMpaDTO(Mpa mpa) {
        MpaDTO dto = new MpaDTO();
        dto.setId(mpa.getId());
        dto.setName(mpa.getName());
        return dto;
    }

    public static Mpa mapDTOToMpa(MpaDTO dto) {
        Mpa mpa = new Mpa();
        mpa.setId(dto.getId());
        mpa.setName(dto.getName());
        return mpa;
    }
}