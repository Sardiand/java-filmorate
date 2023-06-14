package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {
    private final GenreDao genreDaoImpl;

    @Test
    void get() {
        Genre genreCom = genreDaoImpl.get(1).get();
        Genre genreMult = genreDaoImpl.get(3).get();

        assertEquals("Комедия", genreCom.getName());
        assertEquals("Мультфильм", genreMult.getName());
    }

    @Test
    void getAll() {
        ArrayList<Genre> genres = new ArrayList<>(genreDaoImpl.getAll());

        assertEquals("Комедия", genres.get(0).getName());
        assertEquals("Мультфильм", genres.get(2).getName());
    }
}