package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.FilmDirectorRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmDirectorService {
    private final FilmDirectorRepository filmDirectorRepository;
    private final DirectorService directorService;

    public void addDirectorsToFilm(List<Long> directorsIds, Long filmId) {
        filmDirectorRepository.batchInsert(directorsIds, filmId);
    }

    public List<Director> getDirectorsByFilmId(Long filmId) {
        List<FilmDirector> filmDirectors = new ArrayList<>(filmDirectorRepository.findAllByFilmId(filmId));
        return filmDirectors.stream().map(filmDirector ->
                directorService.findDirectorById(filmDirector.getDirectorId())
        ).toList();
    }

    public void deleteFilmDirectors(Long filmId) {
        filmDirectorRepository.deleteFilmDirectors(filmId);
    }

    public Collection<FilmDirector> findAllByDirectorId(Long directorId) {
        return filmDirectorRepository.findAllByDirectorId(directorId);
    }
}
