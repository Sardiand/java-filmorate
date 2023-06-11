package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Inherited
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterFirstFilmReleaseDate {
    String message() default "Field \"releaseDate\" cannot be earlier than 1895-12-28.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
