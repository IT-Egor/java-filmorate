package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.DirectorRepository;
import ru.yandex.practicum.filmorate.storage.FilmDirectorRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;
    private final FilmDirectorRepository filmDirectorRepository;

    public Collection<DirectorDTO> getAllDirectorDTOs() {
        return directorRepository.getAllDirectors().stream()
                .map(DirectorMapper::mapToDirectorDTO)
                .sorted(Comparator.comparing(DirectorDTO::getId))
                .toList();
    }

    public Director findDirectorById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Director with id %s not found", id)));
    }

    public DirectorDTO findDirectorDTOById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Director with id %s not found", id)));
        return DirectorMapper.mapToDirectorDTO(director);
    }

    public DirectorDTO saveDirector(DirectorDTO directorDTO) {
        Director director = DirectorMapper.mapToDirector(directorDTO);
        return findDirector(directorRepository.addDirector(director));
    }

    public DirectorDTO updateDirector(DirectorDTO directorDTO) {
        Director director = DirectorMapper.mapToDirector(directorDTO);
        if (directorRepository.updateDirector(director) == 0) {
            throw new NotFoundException(String.format("Director with id=%s not found", director.getId()));
        }
        return findDirector(director.getId());
    }

    public void removeDirector(Long directorId) {
        directorRepository.removeDirector(directorId);
    }

    public DirectorDTO findDirector(Long id) {
        Optional<Director> directorOpt = directorRepository.findById(id);
        if (directorOpt.isPresent()) {
            return DirectorMapper.mapToDirectorDTO(directorOpt.get());
        } else {
            throw new NotFoundException(String.format("Director with id=%s not found", id));
        }
    }

    public List<Director> getDirectorsByFilmId(Long filmId) {
        List<FilmDirector> filmDirectors = new ArrayList<>(filmDirectorRepository.findAllByFilmId(filmId));
        return filmDirectors.stream().map(filmDirector ->
                findDirectorById(filmDirector.getDirectorId())
        ).toList();
    }

    public List<DirectorDTO> fixIfNullOrWithDuplicates(List<DirectorDTO> directorDTOs) {
        if (directorDTOs == null) {
            return new ArrayList<>();
        } else {
            return directorDTOs.stream().distinct().map(directorDTO -> findDirectorDTOById(directorDTO.getId())).toList();
        }
    }
}
