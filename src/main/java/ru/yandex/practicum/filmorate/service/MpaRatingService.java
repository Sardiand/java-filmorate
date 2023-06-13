package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRatingDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingDaoImpl mpaRatingDaoImpl;

    @Autowired
    public MpaRatingService(MpaRatingDaoImpl mpaRatingDaoImpl) {
        this.mpaRatingDaoImpl = mpaRatingDaoImpl;
    }

    public Mpa getById(Integer id) {
        return mpaRatingDaoImpl.get(id).orElseThrow(() -> new NotFoundException("Not find rating MPA by id: " + id));
    }

    public List<Mpa> getAll() {
        return new ArrayList<>(mpaRatingDaoImpl.getAll());
    }
}
