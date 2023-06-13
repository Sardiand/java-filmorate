package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film createFilm(@NonNull @Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@NonNull @Valid @RequestBody Film film) {
        if (film.getId() == null) {
            BadRequestException exception = new BadRequestException("Field \"id\" of Film can't be null.");
            log.error("Error: " + exception.getMessage());
            throw exception;
        }
        return filmService.update(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        if (count <= 0) {
            throw new BadRequestException("Parameter \"count\" should be more than zero.");
        }
        return filmService.getPopular(count);
    }

    @GetMapping("/films/{id}")
    public Film getUser(@PathVariable Long id) {
        return filmService.getById(id);
    }

}
