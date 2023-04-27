package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.AfterFirstFilmReleaseDate;

import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Field \"name\" cannot be empty.")
    private String name;
    @Size(max = 200, message = "Field \"description\" cannot contain more than 200 symbols.")
    private String description;
    @AfterFirstFilmReleaseDate
    private LocalDate releaseDate;
    @NotNull(message = "Field \"duration\" cannot be empty.")
    @Positive(message = "Value of field \"duration\"must be greater than zero.")
    private Integer duration;
}
