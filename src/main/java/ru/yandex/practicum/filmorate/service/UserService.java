package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {
    private long id = 1L;
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        user.setId(id);
        id++;
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    public User deleteUser(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotExistException("There is no such user in the database.");
        }
        User user = userStorage.getUser(id);
        userStorage.deleteUser(id);
        return user;
    }

    public User getUser(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotExistException("There is no such user in the database.");
        }
        return userStorage.getUser(id);
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public void addFriend(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId) || !userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotExistException("There are no one or both users in the database.");
        } else if (userStorage.getUser(userId).getFriends().contains(friendId)) {
            throw new FriendException("This user have been already added to friends.");
        }
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        user.getFriends().add(friendId);
        userStorage.updateUser(user);

        friend.getFriends().add(userId);
        userStorage.updateUser(friend);
    }

    public void deleteFriend(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId) || !userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotExistException("There are no one or both users in the database.");
        } else if (!userStorage.getUser(userId).getFriends().contains(friendId)) {
            throw new FriendException("This user isn't in your friends list.");
        }
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        user.getFriends().remove(friendId);
        userStorage.updateUser(user);

        friend.getFriends().remove(userId);
        userStorage.updateUser(friend);
    }

    public List<User> getFriends(long userId) {
        return getUser(userId).getFriends().stream().map(this::getUser).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return getUser(userId).getFriends().stream().filter((i) -> getUser(otherId).getFriends().contains(i)).map(this::getUser).collect(Collectors.toList());
    }
}
