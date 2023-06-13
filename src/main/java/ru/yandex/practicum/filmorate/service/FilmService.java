package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmDbStorage;


    public FilmService(FilmStorage filmDbStorage) {
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
        return filmDbStorage.getById(id).orElseThrow(() -> new NotFoundException("Not found film by id: " + id));
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
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
        List<Film> filmList = filmDbStorage.getFilms();
        filmList.sort(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()));
        if (filmList.size() > count) {
            return filmList.subList(0, count);
        } else {
            return filmList;
        }
    }

}
