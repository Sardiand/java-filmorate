package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1L;

    @Override
    public void add(Film film) {
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        log.info("Added film {} released in {} .", film.getName(), film.getReleaseDate().getYear());
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void update(Film film) {
        films.replace(film.getId(), film);
        log.info("Updated information about film {} released in {} .", film.getName(), film.getReleaseDate().getYear());
    }

    @Override
    public void delete(long id) {
        String name = films.get(id).getName();
        LocalDate date = films.get(id).getReleaseDate();
        films.remove(id);
        log.info("Film {} released in {} was deleted.", name, date);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean checkIsExist(Film film) {
        boolean isExist = false;
        for (Film checkingFilm : films.values()) {
            if (film.getName().equals(checkingFilm.getName()) || film.getReleaseDate().equals(checkingFilm.getReleaseDate())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}
