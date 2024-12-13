package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.impl.db.MpaDbStorage;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public MpaDTO findById(Long id) {
        return MpaMapper.mapToDTO(mpaDbStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Mpa with id %s not found", id))));
    }
}
