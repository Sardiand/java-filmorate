package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Long, User> userMap = new HashMap<>();
    private long id = 0L;

    @PostMapping("/users")
    public User createUser(@NonNull @Valid @RequestBody User user) {
        id++;
        user.setId(id);
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userMap.put(id, user);
        log.info("Created user {}", user.getName());
        return userMap.get(id);
    }

    @PutMapping("/users")
    public User updateUser(@NonNull @RequestBody User user) {
        if (!userMap.containsKey(user.getId()) || user.getId() == null) {
            ValidationException exception = new ValidationException("There is no such user in database or field \"id\" is empty.");
            log.error("ValidationException: " + exception.getMessage());
            throw exception;
        } else {
            if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            userMap.replace(id, user);
            log.info("Updated information about user {}", user.getName());
        }
        return userMap.get(id);
    }

    @GetMapping("/users")
    public List<User> getUserList() {
        List<User> users = new ArrayList<>(userMap.values());
        log.info("List of users was sent.");
        return users;
    }
}
