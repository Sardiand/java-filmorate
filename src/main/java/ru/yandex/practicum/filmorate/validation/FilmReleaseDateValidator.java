package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<AfterFirstFilmReleaseDate, LocalDate> {
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date.isEqual(FIRST_FILM_DATE)) {
            return true;
        } else {
            return date.isAfter(FIRST_FILM_DATE);
        }
    }
}
