package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    void add(User user);

    void update(User user);

    void delete(long id);

    List<User> getUsers();

    Optional<User> getById(long id);
}
