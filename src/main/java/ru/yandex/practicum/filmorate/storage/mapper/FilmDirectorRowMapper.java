package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmDirectorRowMapper implements RowMapper<FilmDirector> {
    @Override
    public FilmDirector mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmDirector filmDirector = new FilmDirector();
        filmDirector.setId(rs.getLong("id"));
        filmDirector.setFilmId(rs.getLong("film_id"));
        filmDirector.setDirectorId(rs.getLong("director_id"));
        return filmDirector;
    }
}