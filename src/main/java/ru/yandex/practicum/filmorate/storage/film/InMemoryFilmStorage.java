package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Added film {} released in {} .", film.getName(), film.getReleaseDate().getYear());
    }

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId()) || film.getId() == null) {
            UpdateValidationException exception = new UpdateValidationException("There is no such film in database or field \"id\" is empty.");
            log.error("Error: " + exception.getMessage());
            throw exception;
        } else {
            films.replace(film.getId(), film);
            log.info("Updated information about film {} released in {} .", film.getName(), film.getReleaseDate().getYear());
        }
    }

    @Override
    public void deleteFilm(long id) {
        String name = films.get(id).getName();
        LocalDate date = films.get(id).getReleaseDate();
        films.remove(id);
        log.info("Film {} released in {} was deleted.", name, date);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }
}
