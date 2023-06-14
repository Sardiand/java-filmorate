package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserStorage userDbStorage;

    @Test
    void add() {
        User first = new User(1L, "maxim@maxim.ru", "max+100500", "Maxim",
                LocalDate.of(2009, 3, 1));
        userDbStorage.add(first);

        assertEquals("maxim@maxim.ru", userDbStorage.getById(first.getId()).get().getEmail());
        assertEquals("Maxim", userDbStorage.getById(first.getId()).get().getName());
    }

    @Test
    void update() {
        User first = new User(1L, "maxim@maxim.ru", "max+100500", "Maxim",
                LocalDate.of(2009, 3, 1));
        userDbStorage.add(first);

        assertEquals("maxim@maxim.ru", userDbStorage.getById(first.getId()).get().getEmail());

        first.setEmail("cosmos@cosmos.ru");
        userDbStorage.update(first);

        assertEquals("cosmos@cosmos.ru", userDbStorage.getById(first.getId()).get().getEmail());

        User second = new User(2L, "denis@denis.ru", "Den", "Denis",
                LocalDate.of(1999, 2, 5));
    }

    @Test
    void getById() {
        User first = new User(1L, "maxim@maxim.ru", "max+100500", "Maxim",
                LocalDate.of(2009, 3, 1));
        User second = new User(2L, "denis@denis.ru", "Den", "Denis",
                LocalDate.of(1999, 2, 5));
        userDbStorage.add(first);
        userDbStorage.add(second);
        User testUser = userDbStorage.getById(2L).get();

        assertEquals("Denis", testUser.getName());

        testUser = userDbStorage.getById(1).get();

        assertEquals("Maxim", testUser.getName());
    }

    @Test
    void getUsers() {
        User first = new User(1L, "maxim@maxim.ru", "max+100500", "Maxim",
                LocalDate.of(2009, 3, 1));
        User second = new User(2L, "denis@denis.ru", "Den", "Denis",
                LocalDate.of(1999, 2, 5));
        userDbStorage.add(first);
        userDbStorage.add(second);
        List<User> users = userDbStorage.findUsers();

        assertEquals(2, users.size());
        assertEquals("Den", users.get(1).getLogin());
    }

    @Test
    void makeFinishFriendshipAndGetFriends() {
        User first = new User(1L, "maxim@maxim.ru", "max+100500", "Maxim",
                LocalDate.of(2009, 3, 1));
        User second = new User(2L, "denis@denis.ru", "Den", "Denis",
                LocalDate.of(1999, 2, 5));
        userDbStorage.add(first);
        userDbStorage.add(second);
        userDbStorage.makeFriendship(1,2);

        List<Long> friends = userDbStorage.findFriends(1);

        assertEquals(1, friends.size());
        assertEquals(2, friends.get(0));

        userDbStorage.finishFriendship(1,2);
        friends = userDbStorage.findFriends(1);

        assertTrue(friends.isEmpty());
    }
}