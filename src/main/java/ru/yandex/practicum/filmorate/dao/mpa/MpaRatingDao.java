package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

public interface MpaRatingDao {

    Optional<MpaRating> get(int id);

    Collection<MpaRating> getAll();

}
