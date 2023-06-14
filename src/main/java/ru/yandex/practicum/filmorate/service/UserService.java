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

    @Autowired
    private final UserStorage userDbStorage;

    public UserService(UserStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public User create(User user) {
        user.setNameIfBlank();
        User newUser = userDbStorage.add(user);
        log.info("Created new user {} with id {}.", newUser.getName(), newUser.getId());
        return newUser;
    }

    public User update(User user) {
        if (user.getId() == null || user.getId() <= 0) {
            throw new BadRequestException("Field \"id\" of user is empty or less that 0");
        }
        getById(user.getId());
        user.setNameIfBlank();
        userDbStorage.update(user);
        log.info("Updated information about user {} with id {}.", user.getId(), user.getName());
        return user;
    }

    public User getById(long id) {
        return userDbStorage.getById(id).orElseThrow(() -> new NotFoundException("Not find user by id: " + id));
    }

    public List<User> getUsers() {
        return userDbStorage.findUsers();
    }

    public void addFriend(long userId, long friendId) {
        checkUsers(userId, friendId);
        if (userDbStorage.checkIsFriendshipExist(userId, friendId)) {
            throw new ObjectExistException("User by id " + friendId + " have been already added to friends.");
        }
        userDbStorage.makeFriendship(userId, friendId);
        log.info("User by id {} made friendship with user by id {}.", userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        checkUsers(userId, friendId);
        if (!userDbStorage.checkIsFriendshipExist(userId, friendId)) {
            throw new ObjectExistException("User by id " + friendId + " isn't found in friends list.");
        }
        userDbStorage.finishFriendship(userId, friendId);
        log.info("User by id {} finished friendship with user by id {}.", userId, friendId);
    }

    public List<User> getFriends(long userId) {
        getById(userId);
        log.info("Got friends of user with id {}.", userId);
        return userDbStorage.findFriends(userId).stream().map(this::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        log.info("Got common friends between user by id {} and user by id {}.", userId, otherId);
        return userDbStorage.findFriends(userId).stream().filter((i) -> userDbStorage.findFriends(otherId).contains(i))
                .map(this::getById).collect(Collectors.toList());
    }

    private void checkUsers(long userId, long friendId) {
        getById(userId);
        getById(friendId);
    }
}
