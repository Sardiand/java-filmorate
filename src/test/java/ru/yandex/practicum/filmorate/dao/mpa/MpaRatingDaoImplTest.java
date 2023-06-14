package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRatingDaoImplTest {
    private final MpaRatingDao mpaRatingDaoImpl;

    @Test
    void get() {
        Mpa mpaRatingPG = mpaRatingDaoImpl.get(3).get();
        Mpa mpaRatingNC = mpaRatingDaoImpl.get(5).get();

        assertEquals("PG-13", mpaRatingPG.getName());
        assertEquals("NC-17", mpaRatingNC.getName());
    }

    @Test
    void getAll() {
        ArrayList<Mpa> mpaList = new ArrayList<>(mpaRatingDaoImpl.getAll());

        assertEquals("PG-13", mpaList.get(2).getName());
        assertEquals("NC-17", mpaList.get(4).getName());
    }
}