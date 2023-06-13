package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreDao {

    Optional<Genre> get(int id);

    Optional<Genre> get(String name);

    Collection<Genre> getAll();
}
