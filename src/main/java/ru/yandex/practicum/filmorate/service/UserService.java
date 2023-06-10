package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectExistException;
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
        if (userStorage.checkIsExist(user)) {
            throw new ObjectExistException("User with email " + user.getEmail() + " is already exist");
        }
        user.setNameIfBlank();
        userStorage.add(user);
        return user;
    }

    public User update(User user) {
        getById(user.getId());
        userStorage.update(user);
        return user;
    }

    public User delete(long id) {
        User user = getById(id);
        userStorage.delete(id);
        return user;
    }

    public User getById(long id) {
        return userStorage.getById(id).orElseThrow(() -> new NotFoundException("Not find user by id: " + id));
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(long userId, long friendId) {
        if (getById(userId).getFriends().contains(friendId)) {
            throw new ObjectExistException("User by id " + friendId + " have been already added to friends.");
        }
        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    public void deleteFriend(long userId, long friendId) {
        if (!getById(userId).getFriends().contains(friendId)) {
            throw new ObjectExistException("User by id " + friendId + " isn't found in friends list.");
        }
        User user = getById(userId);
        User friend = getById(friendId);

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
