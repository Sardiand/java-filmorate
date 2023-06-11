package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.List;

public interface FilmStorage {

    void add(Film film);

    void delete(long id);

    void update(Film film);

    boolean checkIsExist(Film film);

    List<Film> getFilms();

    Optional<Film> getById(long id);
}
