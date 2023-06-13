package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Genre {

    @NonNull
    private final Integer id;

    @NonNull
    private final String name;
}
