package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UpdateValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Created user {}", user.getName());
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId()) || user.getId() == null) {
            UpdateValidationException exception = new UpdateValidationException("There is no such user in database or field \"id\" is empty.");
            log.error("UpdateValidationException: " + exception.getMessage());
            throw exception;
        } else {
            if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.replace(user.getId(), user);
            log.info("Updated information about user {}", user.getName());
        }
    }

    @Override
    public void deleteUser(long id) {
        String name = users.get(id).getName();
        users.remove(id);
        log.info("User {} was deleted", name);
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User getUser (long id) {
        return users.get(id);
    }
}
