package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping("/mpa")
    public List<MpaRating> getAllMpaRatings(){
        return mpaRatingService.getAll();
    }

    @GetMapping("/mpa/{}")
    public MpaRating getMpaRatingById(@PathVariable int id) {
        return mpaRatingService.getById(id);
    }
}
