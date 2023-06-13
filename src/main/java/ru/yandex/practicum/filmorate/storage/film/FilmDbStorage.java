package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRatingDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Film> getById(long id) {
        if (jdbcTemplate.query("SELECT * FROM film WHERE film_id=" + id, new FilmMapper(jdbcTemplate,
                new MpaRatingDaoImpl(jdbcTemplate))).isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(jdbcTemplate.query("SELECT * FROM film WHERE film_id=" + id,
                new FilmMapper(jdbcTemplate, new MpaRatingDaoImpl(jdbcTemplate))).get(0));
        }
    }

    public Film add(Film film) {
        jdbcTemplate.update("INSERT INTO film (film_title, film_description, release_date, film_duration, mpa_rating_id)" +
                "VALUES(?, ?, ?, ?, ?)",film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpaRating().getId());
        log.info("Added film {} released in {} .", film.getName(), film.getReleaseDate().getYear());

        String resultQuery = "SELECT * FROM film WHERE film_title=" + film.getName() +
                " AND film_description=" + film.getDescription() +
                " AND release_date=" + Date.valueOf(film.getReleaseDate()) +
                " AND film_duration=" + film.getDuration() +
                " AND mpa_rating_id=" + film.getMpaRating().getId();

        Film addedFilm = jdbcTemplate.query(resultQuery, new FilmMapper(jdbcTemplate, new MpaRatingDaoImpl(jdbcTemplate))).get(0);
        updateGenres(addedFilm.getId(), film);
        return (jdbcTemplate.query(resultQuery, new FilmMapper(jdbcTemplate, new MpaRatingDaoImpl(jdbcTemplate))).get(0));
    }

    public void update(Film film) {
        jdbcTemplate.update("UPDATE film SET film_title=?, film_description=?, release_date=?, film_duration=?, mpa_rating_id=? "
                + "WHERE film_id=?", film.getName(), film.getDescription(),Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpaRating().getId());
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", film.getId());
        updateGenres(film.getId(), film);
        log.info("Updated film {} with id {} .", film.getName(), film.getId());
    }

    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM film ORDER BY film_id", new FilmMapper(jdbcTemplate,
                new MpaRatingDaoImpl(jdbcTemplate)));
        log.info("Got all films.");
        return films;
    }

    public void putLike(long filmId, long userId) {
        jdbcTemplate.update("INSERT INTO film_like (film_id, user_id) VALUES(?, ?)", filmId, userId);
        log.info("User with user_id {} added like to film with film_id {}.", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        jdbcTemplate.update("DELETE FROM film_like WHERE film_id=? AND user_id=?", filmId, userId);
        log.info("Like from user with user_id {} to film with film_id {} was deleted.", userId, filmId);
    }

    public boolean checkIsLikeExist(long filmId, long userId) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM film_like WHERE film_id=? AND user_id=?",
                filmId, userId).next();
    }

    public boolean checkIsFilmExist(Film film) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_title=? AND release_date=?",
                film.getName(), film.getReleaseDate().toString()).next();
    }

    private void updateGenres(long filmId, Film film) {
        if (!film.getGenres().isEmpty()) {
            List<Integer> genres = new ArrayList<>(film.getGenres());
            for (Integer genreId :genres) {
                jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id)" +
                        "VALUES(?, ?)", filmId, genreId);
            }
        }
    }

}
