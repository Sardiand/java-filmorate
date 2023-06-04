package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.AfterFirstFilmReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Field \"name\" cannot be empty.")
    private String name;

    @EqualsAndHashCode.Exclude
    @Size(max = 200, message = "Field \"description\" cannot contain more than 500 symbols.")
    private String description;

    @EqualsAndHashCode.Exclude
    @NotNull(message = "Field \"releaseDate\" cannot be empty.")
    @AfterFirstFilmReleaseDate
    private LocalDate releaseDate;

    @EqualsAndHashCode.Exclude
    @NotNull(message = "Field \"duration\" cannot be empty.")
    @Positive(message = "Value of field \"duration\"must be greater than zero.")
    private Integer duration;

    @EqualsAndHashCode.Exclude
    private final Set<Long> likes = new HashSet<>();
}
