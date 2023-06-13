package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;

    @Test
    void getById() {
        Film film = new Film(1L, "Man", "God choice",
                LocalDate.of(2009, 3, 1), 121, new Mpa(4, "R"));

        filmDbStorage.add(film);

        Film newFilm = filmDbStorage.getById(film.getId()).get();

        assertEquals("Man", newFilm.getName());
    }

    @Test
    void add() {
        Film film = new Film(1L, "Man", "God choice",
                LocalDate.of(2009, 3, 1), 121, new Mpa(4, "R"));

        filmDbStorage.add(film);

        assertEquals(1L, filmDbStorage.getFilms().size());

        assertEquals("R", filmDbStorage.getById(1).get().getMpa().getName());
        assertEquals("Man", filmDbStorage.getById(1).get().getName());
        assertEquals(121, filmDbStorage.getById(1).get().getDuration());
    }

    @Test
    void update() {
        Film film = new Film(1L, "Dorian Grey", "Not Alexandra",
                LocalDate.of(2005, 2, 1), 2, new Mpa(4, "R"));
        filmDbStorage.add(film);
        assertEquals("Dorian Grey", filmDbStorage.getById(1).get().getName());

        film.setName("Boromir");
        filmDbStorage.update(film);
        assertEquals("Boromir", filmDbStorage.getById(1).get().getName());
    }

    @Test
    void getFilms() {
        Film one = new Film(1L, "Man", "God choice",
                LocalDate.of(2009, 3, 1), 121, new Mpa(4, "R"));

        Film two = new Film(2L, "Dorian Grey", "Not Alexandra",
                LocalDate.of(2005, 2, 1), 2, new Mpa(4, "R"));

        Film three = new Film(3L, "Heroes", "But not 100 percent",
                LocalDate.of(2002, 2, 2), 2, new Mpa(3, "PG-13"));

        filmDbStorage.add(one);
        filmDbStorage.add(two);
        filmDbStorage.add(three);

        List<Film> films = filmDbStorage.getFilms();

        assertEquals(3, films.size());
        assertEquals("Heroes", films.get(2).getName());
    }

    @Test
    void putDeleteLike() {
        Film one = new Film(1L, "Man", "God choice",
                LocalDate.of(2009, 3, 1), 121, new Mpa(4, "R"));

        User first = new User(1L, "maxim@maxim.ru", "max+100500", "Maxim",
                LocalDate.of(2009, 3, 1));

        User second = new User(2L, "denis@denis.ru", "Den", "Denis",
                LocalDate.of(1999, 2, 5));

        filmDbStorage.add(one);
        userDbStorage.add(first);
        userDbStorage.add(second);

        filmDbStorage.putLike(one.getId(), first.getId());
        filmDbStorage.putLike(one.getId(), second.getId());

        List<Long> likes = filmDbStorage.getLikes(one.getId());

        assertEquals(2, likes.size());
        assertEquals(1, likes.get(0));

        filmDbStorage.removeLike(one.getId(), second.getId());
        likes = filmDbStorage.getLikes(one.getId());

        assertEquals(1, likes.size());
    }
}