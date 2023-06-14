package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int rating = (rs.getInt("mpa_rating_id"));

        return new Film(rs.getLong("film_id"),
                rs.getString("film_title"),
                rs.getString("film_description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("film_duration"),
                new Mpa(rating));
    }
}

