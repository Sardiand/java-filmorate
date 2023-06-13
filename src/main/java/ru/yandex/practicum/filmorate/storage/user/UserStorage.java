package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {

    User add(User user);

    void update(User user);

    List<User> getUsers();

    Optional<User> getById(long id);

    void makeFriendship(long userId, long friendId);

    void finishFriendship(long userId, long friendId);

    boolean checkIsFriendshipExist(long userId, long friendId);

    List<Long> getFriends(long id);
}
