package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    private void setJdbcInsert(JdbcTemplate jdbcTemplate) {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    @Override
    public Optional<Film> findById(long id) {
        String sql = "SELECT * FROM film WHERE film_id=" + id;
        if (jdbcTemplate.query(sql, new FilmMapper()).isEmpty()) {
            return Optional.empty();
        } else {
            Film film = jdbcTemplate.query(sql, new FilmMapper()).get(0);
            return Optional.of(film);
        }
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO film (film_title, film_description, release_date, film_duration, mpa_rating_id)" +
                "VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, film.getName());
            pst.setString(2, film.getDescription());
            pst.setDate(3, Date.valueOf(film.getReleaseDate()));
            pst.setInt(4, film.getDuration());
            pst.setInt(5, film.getMpa().getId());
            return pst;
        }, keyHolder);
        Long filmId = keyHolder.getKey().longValue();
        updateGenres(filmId, film);
        film.setId(filmId);
        log.info("Added film {} released in {} .", film.getName(), film.getReleaseDate().getYear());

        return film;
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update("UPDATE film SET film_title=?, film_description=?, release_date=?, film_duration=?, mpa_rating_id=? "
                        + "WHERE film_id=?", film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", film.getId());
        updateGenres(film.getId(), film);
        log.info("Updated film {} with id {} .", film.getName(), film.getId());
    }

    @Override
    public List<Film> findFilms() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM film ORDER BY film_id", new FilmMapper());
        log.info("Got all films.");
        return films;
    }

    @Override
    public void putLike(long filmId, long userId) {
        jdbcTemplate.update("INSERT INTO film_like (film_id, user_id) VALUES(?, ?)", filmId, userId);
        log.info("User with user_id {} added like to film with film_id {}.", userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        jdbcTemplate.update("DELETE FROM film_like WHERE film_id=? AND user_id=?", filmId, userId);
        log.info("Like from user with user_id {} to film with film_id {} was deleted.", userId, filmId);
    }

    @Override
    public List<Long> findLikes(long filmId) {
        return jdbcTemplate.queryForList("SELECT user_id FROM film_like WHERE film_id = ?",
                Long.class, filmId);
    }

    @Override
    public boolean checkIsLikeExist(long filmId, long userId) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM film_like WHERE film_id=? AND user_id=?",
                filmId, userId).next();
    }

    @Override
    public List<Film> findPopular(int count) {
        String sql = "SELECT * FROM film AS f " +
                "LEFT OUTER JOIN film_like fl on f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.film_id) " +
                "DESC LIMIT ?";
        return jdbcTemplate.query(sql, new FilmMapper(), count);
    }

    @Override
    public boolean checkIsFilmExist(Film film) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_title=? AND release_date=?",
                film.getName(), film.getReleaseDate().toString()).next();
    }

    private void updateGenres(long filmId, Film film) {
        if (!film.getGenres().isEmpty()) {
            List<Genre> genres = new ArrayList<>(film.getGenres());
            for (Genre genre : genres) {
                jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id)" +
                        "VALUES(?, ?)", filmId, genre.getId());
            }
        }
    }

    private void setMpa(Film film) {

    }


}
