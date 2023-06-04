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
            throw new BadRequestException("This film have already been added.");
        }
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film) {
        if (film.getId() == null || filmStorage.getById(film.getId()).isEmpty()) {
            NotFoundException exception = new NotFoundException("There is no such film in database or field \"id\" is empty.");
            log.error("Error: " + exception.getMessage());
            throw exception;
        }
        filmStorage.update(film);
        return film;
    }

    public Film delete(long id) {
        if (filmStorage.getById(id).isEmpty()) {
            throw new NotFoundException("There is no such film in the database.");
        }
        Film film = filmStorage.getById(id).get();
        filmStorage.delete(id);
        return film;
    }

    public Film getById(long id) {
        if (filmStorage.getById(id).isEmpty()) {
            throw new NotFoundException("There is no such film in the database.");
        }
        return filmStorage.getById(id).get();
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(long filmId, long userId) {
        if (filmStorage.getById(filmId).isEmpty()) {
            throw new NotFoundException("There is no such film in the database.");
        }
        Film film = filmStorage.getById(filmId).get();
        if (film.getLikes().contains(userId)) {
            throw new NotFoundException("You have already liked this film.");
        }
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getById(filmId).isEmpty()) {
            throw new NotFoundException("There is no such film in the database.");
        }
        Film film = filmStorage.getById(filmId).get();
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("You haven't liked this film yet.");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(int count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        List<Film> filmList = filmStorage.getFilms();
        filmList.sort(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()));
        if (filmList.size() > count) {
            return filmList.subList(0, count);
        } else {
            return filmList;
        }
    }
}
