package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;

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
        Director director = findDirectorById(id);
        return DirectorMapper.mapToDirectorDTO(director);
    }

    public DirectorDTO saveDirector(DirectorDTO directorDTO) {
        Director director = DirectorMapper.mapToDirector(directorDTO);
        long addedDirectorId = directorRepository.addDirector(director);
        directorDTO.setId(addedDirectorId);
        return directorDTO;
    }

    public DirectorDTO updateDirector(DirectorDTO directorDTO) {
        Director director = DirectorMapper.mapToDirector(directorDTO);
        if (directorRepository.updateDirector(director) == 0) {
            throw new NotFoundException(String.format("Director with id=%s not found", director.getId()));
        }
        return directorDTO;
    }

    public void removeDirector(Long directorId) {
        directorRepository.removeDirector(directorId);
    }

    public List<Director> getDirectorsByFilmId(Long filmId) {
        return new ArrayList<>(directorRepository.findAllByFilmId(filmId));
    }

    public void addDirectorsToFilm(List<Long> directorsIds, Long filmId) {
        directorRepository.batchInsert(directorsIds, filmId);
    }

    public void deleteFilmDirectors(Long filmId) {
        directorRepository.deleteFilmDirectors(filmId);
    }

    public List<DirectorDTO> fixIfNullOrWithDuplicates(List<DirectorDTO> directorDTOs) {
        if (directorDTOs == null) {
            return new ArrayList<>();
        } else {
            return directorDTOs.stream().distinct().map(directorDTO -> findDirectorDTOById(directorDTO.getId())).toList();
        }
    }

    public Map<Long, List<Director>> findAllByManyFilmIds(Collection<Long> filmIds) {
        return directorRepository.findAllByManyFilmIds(filmIds);
    }
}
