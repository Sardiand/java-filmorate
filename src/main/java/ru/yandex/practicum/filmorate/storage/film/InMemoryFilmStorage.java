package ru.yandex.practicum.filmorate.storage.film;


import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;


@Component("memoryFilmStorage")
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0L;

    @Override
    public Film add(Film film) {
        id++;
        film.setId(id);
        films.put(film.getId(), film);
        return films.get(id);
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void update(Film film) {
        films.replace(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean checkIsFilmExist(Film film) {
        boolean isExist = false;
        for (Film checkingFilm : films.values()) {
            if (film.getName().equals(checkingFilm.getName()) && film.getReleaseDate().equals(checkingFilm.getReleaseDate())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    @Override
    public void putLike(long filmId, long userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    public List<Long> getLikes(long filmId) {
        return new ArrayList<>(films.get(filmId).getLikes());
    }

    @Override
    public boolean checkIsLikeExist(long filmId, long userId) {
        return films.get(filmId).getLikes().contains(userId);
    }
}
