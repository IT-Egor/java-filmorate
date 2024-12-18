package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<MpaDTO> getAll() {
        return mpaService.getAllMpaDTOs();
    }

    @GetMapping("/{mpaId}")
    public MpaDTO getGenreById(@PathVariable Long mpaId) {
        return mpaService.findDTOById(mpaId);
    }
}
