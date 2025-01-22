package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorRepository;
import ru.yandex.practicum.filmorate.storage.FilmDirectorRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

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
        Director director = findDirectorById(id);
        return DirectorMapper.mapToDirectorDTO(director);
    }

    public DirectorDTO saveDirector(DirectorDTO directorDTO) {
        Director director = DirectorMapper.mapToDirector(directorDTO);
        return findDirectorDTOById(directorRepository.addDirector(director));
    }

    public DirectorDTO updateDirector(DirectorDTO directorDTO) {
        Director director = DirectorMapper.mapToDirector(directorDTO);
        if (directorRepository.updateDirector(director) == 0) {
            throw new NotFoundException(String.format("Director with id=%s not found", director.getId()));
        }
        return findDirectorDTOById(director.getId());
    }

    public void removeDirector(Long directorId) {
        directorRepository.removeDirector(directorId);
    }

    public List<Director> getDirectorsByFilmId(Long filmId) {
        return new ArrayList<>(directorRepository.findAllByFilmId(filmId));
    }

    public void addDirectorsToFilm(List<Long> directorsIds, Long filmId) {
        filmDirectorRepository.batchInsert(directorsIds, filmId);
    }

    public void deleteFilmDirectors(Long filmId) {
        filmDirectorRepository.deleteFilmDirectors(filmId);
    }

    public List<DirectorDTO> fixIfNullOrWithDuplicates(List<DirectorDTO> directorDTOs) {
        if (directorDTOs == null) {
            return new ArrayList<>();
        } else {
            return directorDTOs.stream().distinct().map(directorDTO -> findDirectorDTOById(directorDTO.getId())).toList();
        }
    }
}
