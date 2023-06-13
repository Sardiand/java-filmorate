package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.dao.genre.GenreMapper;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FilmMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingDao mpaRatingDao;

    public FilmMapper(JdbcTemplate jdbcTemplate, MpaRatingDao mpaRatingDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingDao = mpaRatingDao;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("film_id");
        List<Genre> filmGenres = getGenres(filmId);

        int rating = (rs.getInt("mpa_rating_id"));
        Film film = new Film(rs.getLong("film_id"),
                rs.getString("film_title"),
                rs.getString("film_description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("film_duration"));

        film.setMpa(mpaRatingDao.get(rating).orElseThrow(() -> new NotFoundException("Not found MPA rating.")));
        if (!filmGenres.isEmpty()) {
            for(Genre genre: filmGenres){
                film.getGenres().add(genre);
            }
        }
        return film;
    }

    private List<Genre> getGenres(long filmId) {
        return jdbcTemplate.query("SELECT fg.genre_id, genre.name FROM film_genre AS fg LEFT OUTER JOIN " +
                "genre ON fg.genre_id=genre.genre_id WHERE film_id=" +
                filmId, new GenreMapper());
    }
}

