package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long UserId = 1L;

    @Override
    public void add(User user) {
        user.setId(UserId);
        UserId++;
        users.put(user.getId(), user);
        log.info("Created user {}", user.getName());
    }

    @Override
    public void update(User user) {
        users.replace(user.getId(), user);
        log.info("Updated information about user {}", user.getName());

    }

    @Override
    public void delete(long id) {
        String name = users.get(id).getName();
        users.remove(id);
        log.info("User {} was deleted", name);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
