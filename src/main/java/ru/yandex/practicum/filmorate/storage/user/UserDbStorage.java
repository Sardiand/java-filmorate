package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.user.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Data
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    private void setJdbcInset(JdbcTemplate jdbcTemplate) {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1,user.getEmail());
            pst.setString(2, user.getLogin());
            pst.setString(3, user.getName());
            pst.setDate(4, Date.valueOf(user.getBirthday()));
            return pst;
        }, keyHolder);

        Long userId = keyHolder.getKey().longValue();
        user.setId(userId);
        log.info("Added new user - {}.", user.getName());

        return user;
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? WHERE user_id=?",
                user.getEmail(),user.getLogin(),user.getName(), Date.valueOf(user.getBirthday()), user.getId());
            jdbcTemplate.update("DELETE FROM user_friendship WHERE from_user_id=?", user.getId());
        if (!user.getFriends().isEmpty()) {
            for (Long i : user.getFriends()) {
                jdbcTemplate.update("INSERT INTO user_friendship (from_user_id," +
                        " to_user_id) VALUES (?, ?)", user.getId(), i);
            }
        }
    }

    @Override
    public Optional<User> getById(long id) {
        if (jdbcTemplate.query("SELECT * FROM users WHERE user_id=" + id, new UserMapper()).isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(jdbcTemplate.query("SELECT * FROM users WHERE user_id=" + id, new UserMapper()).get(0));
        }
    }

    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public void makeFriendship(long userId, long friendId) {
        jdbcTemplate.update("INSERT INTO user_friendship (from_user_id, to_user_id) VALUES (?, ?)",
                userId, friendId);
    }

    @Override
    public void finishFriendship(long userId, long friendId) {
        jdbcTemplate.update("DELETE FROM user_friendship WHERE from_user_id=? AND to_user_id=?",
                userId, friendId);
    }

    @Override
    public List<Long> getFriends(long id) {
        return jdbcTemplate.queryForList("SELECT to_user_id FROM user_friendship WHERE from_user_id="
                + id + " ORDER BY to_user_id", Long.class);
    }

    @Override
    public boolean checkIsFriendshipExist(long userId, long friendId) {
        return jdbcTemplate.queryForRowSet("SELECT * FROM user_friendship WHERE from_user_id=? AND to_user_id=?",
                userId, friendId).next();
    }
}
