package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private static Validator validator;
    private Film film;
    private List<String> violations;
    private FilmController controller;

    @BeforeAll
    static void setValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void makeNewFilm() {
        film = new Film();
        film.setName("Вызов");
        film.setDescription("First film that was made in space. But nobody knows what's the point.");
        film.setReleaseDate(LocalDate.of(2023, 4, 20));
        film.setDuration(164);
        controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
    }

    @Test
    void checkWrongNameValidation() {
        film.setName(null);
        Set<ConstraintViolation<Film>> validateNotNull = validator.validate(film);
        violations = makeViolationList(validateNotNull);
        assertEquals("Field \"name\" cannot be empty.", violations.get(0));

        film.setName("");
        Set<ConstraintViolation<Film>> validateNotEmpty = validator.validate(film);
        violations = makeViolationList(validateNotEmpty);
        assertEquals("Field \"name\" cannot be empty.", violations.get(0));

        film.setName("      ");
        Set<ConstraintViolation<Film>> validateNotBlank = validator.validate(film);
        violations = makeViolationList(validateNotBlank);
        assertEquals("Field \"name\" cannot be empty.", violations.get(0));
    }

    @Test
    void checkWrongDescriptionValidation() {
        byte[] array = new byte[999];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        film.setDescription(generatedString);
        Set<ConstraintViolation<Film>> validateNotMoreThanTwoHundreds = validator.validate(film);
        violations = makeViolationList(validateNotMoreThanTwoHundreds);
        assertEquals("Field \"description\" cannot contain more than 200 symbols.", violations.get(0));
    }

    @Test
    void checkWrongReleaseDateValidation() throws ValidationException, NullPointerException {
        film.setReleaseDate(null);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            Set<ConstraintViolation<Film>> validateWrongDate = validator.validate(film);
        }, "ValidationException was expected.");

        film.setReleaseDate(LocalDate.of(1861, 2, 19));
        Set<ConstraintViolation<Film>> validateWrongDate = validator.validate(film);
        violations = makeViolationList(validateWrongDate);
        assertEquals("Field \"releaseDate\" cannot be earlier than 1895-12-28.", violations.get(0));
    }

    @Test
    void checkWrongDurationValidation() {
        film.setDuration(null);
        Set<ConstraintViolation<Film>> validateNullDuration = validator.validate(film);
        violations = makeViolationList(validateNullDuration);
        assertEquals("Field \"duration\" cannot be empty.", violations.get(0));

        film.setDuration(0);
        Set<ConstraintViolation<Film>> validateWrongDuration = validator.validate(film);
        violations = makeViolationList(validateWrongDuration);
        assertEquals("Value of field \"duration\"must be greater than zero.", violations.get(0));
    }

    @Test
    void checkSendingEmptyRequestForCreationUser() throws NullPointerException {
        Film emptyFilm = null;
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            controller.createFilm(emptyFilm);
        }, "NullPointerException was expected.");
        assertEquals("film is marked non-null but is null", thrown.getMessage());
    }

    @Test
    void checkUpdatingWithEmptyRequestOrWithWrongId() {
        Film emptyFilm = null;
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            controller.updateFilm(emptyFilm);
        }, "NullPointerException was expected.");
        assertEquals("film is marked non-null but is null", thrown.getMessage());

        film.setId(null);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            controller.updateFilm(film);
        }, "NotFoundException was expected.");
        assertEquals("There is no such film in database or field \"id\" is empty.", exception.getMessage());

        film.setId(138L);
        exception = assertThrows(NotFoundException.class, () -> {
            controller.updateFilm(film);
        }, "NotFoundException was expected.");
        assertEquals("There is no such film in database or field \"id\" is empty.", exception.getMessage());
    }

    private List<String> makeViolationList(Set<ConstraintViolation<Film>> validate) {
        List<String> violations = new ArrayList<>();
        for (ConstraintViolation<Film> violation : validate) {
            violations.add(violation.getMessage());
        }
        return violations;
    }
}