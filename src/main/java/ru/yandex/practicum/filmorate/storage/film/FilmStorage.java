package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.List;

@Component
public interface FilmStorage {

    Film add(Film film);

    void update(Film film);

    boolean checkIsFilmExist(Film film);

    List<Film> findFilms();

    Optional<Film> findById(long id);

    List<Film> findPopular(int count);

    void putLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    boolean checkIsLikeExist(long filmId, long userId);

    List<Long> findLikes(long id);
}
