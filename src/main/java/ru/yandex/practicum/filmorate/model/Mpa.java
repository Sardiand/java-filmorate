package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor()
@AllArgsConstructor
public class Mpa {

    private final Integer id;
    private String name;
}
