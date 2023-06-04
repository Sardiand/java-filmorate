package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private static Validator validator;
    private UserController controller;

    private User user;
    private List<String> violations;

    @BeforeAll
    static void setValidatorAndControllers() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void makeNewUser() {
        user = new User();
        user.setLogin("login");
        user.setEmail("user@yahoo.com");
        user.setBirthday(LocalDate.of(1952, 10, 7));
        controller = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void checkWrongEmailValidation() {
        user.setEmail("1921shot");
        Set<ConstraintViolation<User>> validateFormat = validator.validate(user);
        violations = makeViolationList(validateFormat);
        assertEquals("Invalid email format.", violations.get(0));

        user.setEmail(null);
        Set<ConstraintViolation<User>> validateNotNull = validator.validate(user);
        violations = makeViolationList(validateNotNull);
        assertEquals("Invalid email format.", violations.get(0));

        user.setEmail("");
        Set<ConstraintViolation<User>> validateNotEmpty = validator.validate(user);
        violations = makeViolationList(validateNotEmpty);
        assertEquals("Invalid email format.", violations.get(0));

        user.setEmail("         ");
        Set<ConstraintViolation<User>> validateNotBlank = validator.validate(user);
        violations = makeViolationList(validateNotBlank);
        assertEquals("Invalid email format.", violations.get(0));
    }

    @Test
    void checkWrongLoginValidation() {
        user.setLogin("Doctor Who");
        Set<ConstraintViolation<User>> validateFormat = validator.validate(user);
        violations = makeViolationList(validateFormat);
        assertEquals("Field \"login\" cannot contain whitespaces.", violations.get(0));

        user.setLogin(null);
        Set<ConstraintViolation<User>> validateNotNull = validator.validate(user);
        violations = makeViolationList(validateNotNull);
        assertEquals("Field \"login\" cannot be empty.", violations.get(0));
    }

    @Test
    void checkWrongBirthdayValidation() {
        user.setBirthday(LocalDate.of(2025, 12, 13));
        Set<ConstraintViolation<User>> validateDate = validator.validate(user);
        violations = makeViolationList(validateDate);
        assertEquals("Value of field \"birthday\" cannot be in the future.", violations.get(0));
    }

    @Test
    void checkSendingEmptyRequestForCreationUser() {
        User emptyUser = null;

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            controller.createUser(emptyUser);
        }, "NullPointerException was expected.");
        assertEquals("user is marked non-null but is null", thrown.getMessage());
    }

    @Test
    void checkUpdatingWithEmptyRequestOrWithWrongId() {
        User emptyUser = null;
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> controller.updateUser(emptyUser), "NullPointerException was expected.");
        assertEquals("user is marked non-null but is null", thrown.getMessage());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            controller.updateUser(user);
        }, "NotFoundException was expected.");
        assertEquals("There is no such user in database or field \"id\" is empty.", exception.getMessage());

        user.setId(138L);
        exception = assertThrows(NotFoundException.class, () -> {
            controller.updateUser(user);
        }, "NotFoundException was expected.");
        assertEquals("There is no such user in database or field \"id\" is empty.", exception.getMessage());
    }

    private List<String> makeViolationList(Set<ConstraintViolation<User>> validate) {
        List<String> violations = new ArrayList<>();
        for (ConstraintViolation<User> violation : validate) {
            violations.add(violation.getMessage());
        }
        return violations;
    }

}