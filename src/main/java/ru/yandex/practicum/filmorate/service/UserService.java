package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userStorage.add(user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null || userStorage.getById(user.getId()).isEmpty()) {
            NotFoundException exception = new NotFoundException("There is no such user in database or field \"id\" is empty.");
            log.error("UpdateValidationException: " + exception.getMessage());
            throw exception;
        }
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userStorage.update(user);
        return user;
    }

    public User delete(long id) {
        if (userStorage.getById(id).isEmpty()) {
            throw new NotFoundException("There is no such user in the database.");
        }
        User user = userStorage.getById(id).get();
        userStorage.delete(id);
        return user;
    }

    public User getById(long id) {
        if (userStorage.getById(id).isEmpty()) {
            throw new NotFoundException("There is no such user in the database.");
        }
        return userStorage.getById(id).get();
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(long userId, long friendId) {
        if (userStorage.getById(userId).isEmpty() || userStorage.getById(friendId).isEmpty()) {
            throw new NotFoundException("There are no one or both users in the database.");
        } else if (userStorage.getById(userId).get().getFriends().contains(friendId)) {
            throw new BadRequestException("This user have been already added to friends.");
        }
        User user = userStorage.getById(userId).get();
        User friend = userStorage.getById(friendId).get();

        user.getFriends().add(friendId);
        userStorage.update(user);

        friend.getFriends().add(userId);
        userStorage.update(friend);
    }

    public void deleteFriend(long userId, long friendId) {
        if (userStorage.getById(userId).isEmpty() || userStorage.getById(friendId).isEmpty()) {
            throw new NotFoundException("There are no one or both users in the database.");
        } else if (!userStorage.getById(userId).get().getFriends().contains(friendId)) {
            throw new BadRequestException("This user isn't in your friends list.");
        }
        User user = userStorage.getById(userId).get();
        User friend = userStorage.getById(friendId).get();

        user.getFriends().remove(friendId);
        userStorage.update(user);

        friend.getFriends().remove(userId);
        userStorage.update(friend);
    }

    public List<User> getFriends(long userId) {
        return getById(userId).getFriends().stream().map(this::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return getById(userId).getFriends().stream().filter((i) -> getById(otherId).getFriends().contains(i)).map(this::getById).collect(Collectors.toList());
    }
}
