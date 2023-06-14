package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreMapper;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Slf4j
@Service

public class FilmService {

    private final JdbcTemplate jdbcTemplate;

    @Qualifier("filmDbStorage")
    private final FilmStorage filmDbStorage;

    public FilmService(JdbcTemplate jdbcTemplate, FilmStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
    }

    public Film create(Film film) {
        if (filmDbStorage.checkIsFilmExist(film)) {
            throw new ObjectExistException("This film have already been added.");
        }
        Film createdFilm = filmDbStorage.add(film);
        log.info("Created film {} with id {}.", createdFilm.getName(), createdFilm.getId());
        return createdFilm;
    }

    public Film update(Film film) {
        getById(film.getId());
        filmDbStorage.update(film);
        log.info("Updated film {} with id {} .", film.getName(), film.getId());
        return getById(film.getId());
    }

    public Film getById(long id) {
        Film film = filmDbStorage.findById(id).orElseThrow(() -> new NotFoundException("Not found film by id: " + id));
        getGenres(id).forEach(g -> film.getGenres().add(g));
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDbStorage.findFilms();

        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM genre gr," +
                " film_genre fgr WHERE fgr.genre_id = gr.genre_id AND fgr.film_id IN (" + filmIds + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getLong("film_id"));
            film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
        });

        return films;
    }

    public void addLike(long filmId, long userId) {
        getById(filmId);
        if (filmDbStorage.checkIsLikeExist(filmId, userId)) {
            throw new ObjectExistException("You have already liked this film.");
        }
        filmDbStorage.putLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        getById(filmId);
        if (!filmDbStorage.checkIsLikeExist(filmId, userId)) {
            throw new NotFoundException("You haven't liked this film yet.");
        }
        filmDbStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmDbStorage.findPopular(count);
    }

    private List<Genre> getGenres(long filmId) {
        return jdbcTemplate.query("SELECT fg.genre_id, genre.name FROM film_genre AS fg LEFT OUTER JOIN " +
                "genre ON fg.genre_id=genre.genre_id WHERE film_id=" +
                filmId, new GenreMapper());
    }
}
