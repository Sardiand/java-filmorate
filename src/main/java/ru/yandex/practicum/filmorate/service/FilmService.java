package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        if (filmStorage.checkIsExist(film)) {
            throw new ObjectExistException("This film have already been added.");
        }
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film) {
        getById(film.getId());
        filmStorage.update(film);
        return film;
    }

    public Film delete(long id) {
        Film film = getById(id);
        filmStorage.delete(id);
        return film;
    }

    public Film getById(long id) {
        return filmStorage.getById(id).orElseThrow(() -> new NotFoundException("Not found film by id: " + id));
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (getById(filmId).getLikes().contains(userId)) {
            throw new ObjectExistException("You have already liked this film.");
        }
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("You haven't liked this film yet.");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(int count) {
        List<Film> filmList = filmStorage.getFilms();
        filmList.sort(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()));
        if (filmList.size() > count) {
            return filmList.subList(0, count);
        } else {
            return filmList;
        }
    }
}
