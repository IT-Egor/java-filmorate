package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<DirectorDTO> getAll() {
        return directorService.getAllDirectorDTOs();
    }

    @GetMapping("/{directorId}")
    public DirectorDTO getDirectorById(@PathVariable Long directorId) {
        return directorService.findDirectorDTOById(directorId);
    }

    @PostMapping
    public DirectorDTO createDirector(@Valid @RequestBody DirectorDTO director) {
        return directorService.saveDirector(director);
    }

    @PutMapping
    public DirectorDTO updateDirector(@Valid @RequestBody DirectorDTO director) {
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{directorId}")
    public void removeDirector(@PathVariable Long directorId) {
        directorService.removeDirector(directorId);
    }
}
