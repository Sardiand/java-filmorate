package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistsException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.LikeIsExistException;
import ru.yandex.practicum.filmorate.exception.LikeNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private long id = 1L;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        film.setId(id);
        id++;
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return film;
    }

    public Film deleteFilm(long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new FilmNotExistsException("There is no such film in the database.");
        }
        Film film = filmStorage.getFilm(id);
        filmStorage.deleteFilm(id);
        return film;
    }

    public Film getFilmById(long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new FilmNotExistsException("There is no such film in the database.");
        }
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public void addLike(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotExistsException("There is no such film in the database.");
        }
        Film film = filmStorage.getFilm(filmId);
        if (film.getLikes().contains(userId)) {
            throw new LikeIsExistException("You have already liked this film.");
        }
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotExistsException("There is no such film in the database.");
        }
        Film film = filmStorage.getFilm(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new LikeNotExistException("You haven't liked this film yet.");
        }
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        List<Film> filmList = new ArrayList<>(filmStorage.getFilms().values());
        filmList.sort(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()));
        if (filmList.size() > count) {
            return filmList.subList(0, count);
        } else {
            return filmList;
        }
    }
}
