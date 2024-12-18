package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaRepository;

import java.util.Collection;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public MpaDTO findDTOById(Long id) {
        return MpaMapper.mapToMpaDTO(mpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Mpa with id %s not found", id))));
    }

    public Mpa findMpaById(Long id) {
        return mpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Mpa with id %s not found", id)));
    }

    public Collection<MpaDTO> getAllMpaDTOs() {
        return mpaRepository.findAll().stream().map(MpaMapper::mapToMpaDTO)
                .sorted(Comparator.comparing(MpaDTO::getId)).toList();
    }
}
