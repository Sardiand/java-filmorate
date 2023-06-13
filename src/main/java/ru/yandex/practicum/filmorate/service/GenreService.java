package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {
    private final GenreDaoImpl genreDaoImpl;

    @Autowired
    public GenreService(GenreDaoImpl genreDaoImpl) {
        this.genreDaoImpl = genreDaoImpl;
    }

    public Genre getById(Integer id) {
        return genreDaoImpl.get(id).orElseThrow(() -> new NotFoundException("Not find genre by id: " + id));
    }

    public List<Genre> getAll(){
        return new ArrayList<>(genreDaoImpl.getAll());
    }
}
