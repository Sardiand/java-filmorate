package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.AfterFirstFilmReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Field \"name\" cannot be empty.")
    private String name;

    @EqualsAndHashCode.Exclude
    @Size(max = 200, message = "Field \"description\" cannot contain more than 200 symbols.")
    private String description;

    @EqualsAndHashCode.Exclude
    @NotNull(message = "Field \"releaseDate\" cannot be empty.")
    @AfterFirstFilmReleaseDate
    private LocalDate releaseDate;

    @EqualsAndHashCode.Exclude
    @NotNull(message = "Field \"duration\" cannot be empty.")
    @Positive(message = "Value of field \"duration\"must be greater than zero.")
    private Integer duration;

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @EqualsAndHashCode.Exclude
    private Set<Long> likes = new HashSet<>();

    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();


}
