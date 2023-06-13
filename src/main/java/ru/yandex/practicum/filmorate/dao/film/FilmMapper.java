package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.dao.mpa.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        List<Integer> filmGenres = getGenres(filmId);

        int rating = (rs.getInt("mpa_rating_id"));
        Film film = new Film(rs.getLong("film_id"),
                rs.getString("film_title"),
                rs.getString("film_description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("film_duration"));

        film.setMpaRating(mpaRatingDao.get(rating).orElseThrow(() -> new NotFoundException("Not found MPA rating.")));
        if (!filmGenres.isEmpty()) {
            for(Integer genreId: filmGenres){
                film.getGenres().add(genreId);
            }
        }
        return film;
    }

    private List<Integer> getGenres(long filmId) {
        return jdbcTemplate.queryForList("SELECT genre_id FROM film_genre WHERE film_id=" +
                filmId, Integer.class);
    }
}

