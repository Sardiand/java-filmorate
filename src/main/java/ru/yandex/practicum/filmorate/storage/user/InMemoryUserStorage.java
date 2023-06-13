package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Data
@Component("memoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1L;

    @Override
    public User add(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return users.get(id);
    }

    @Override
    public void update(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void makeFriendship(long userId, long friendId) {
        users.get(userId).getFriends().add(friendId);
    }

    @Override
    public void finishFriendship(long userId, long friendId) {
        users.get(userId).getFriends().remove(friendId);
        log.info("User {} finished friendship with user {}.", users.get(userId).getName(),
                users.get(friendId).getName());
    }

    @Override
    public boolean checkIsFriendshipExist(long userId, long friendId) {
        return users.get(userId).getFriends().contains(friendId);
    }

    @Override
    public List<Long> getFriends(long id) {
        return new ArrayList<>(users.get(id).getFriends());
    }
}
