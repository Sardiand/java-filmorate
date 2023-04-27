package ru.yandex.practicum.filmorate.controller;


import javax.validation.Valid;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final Map<Long, Film> filmMap = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long id = 0L;

    @PostMapping("/films")
    public Film createFilm(@NonNull @Valid @RequestBody Film film) {
        id++;
        film.setId(id);
        filmMap.put(id, film);
        log.info("Added film {} released in {} .", film.getName(), film.getReleaseDate().getYear());
        return filmMap.get(id);
    }

    @PutMapping("/films")
    public Film updateFilm(@NonNull @Valid @RequestBody Film film) {
        if (!filmMap.containsKey(film.getId()) || film.getId() == null) {
            ValidationException exception = new ValidationException("There is no such movie in database or field \"id\" is empty.");
            log.error("Error: " + exception.getMessage());
            throw exception;
        } else {
            filmMap.replace(id, film);
            log.info("Updated information about film {} released in {} .", film.getName(), film.getReleaseDate().getYear());
        }
        return filmMap.get(id);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<Film>(filmMap.values());
        log.info("List of films was sent.");
        return films;
    }
}
