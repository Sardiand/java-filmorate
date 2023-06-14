package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;
import java.util.List;

@Slf4j
@Data
@Component
public class MpaRatingDaoImpl implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> get(int id) {
        String sql = "SELECT * FROM mpa_rating WHERE mpa_rating_id=?";
        if (jdbcTemplate.query(sql, new MpaRatingMapper(), id).isEmpty()) {
            log.info("Rating MPA by id {} is not found.", id);
            return Optional.empty();
        } else {
            Mpa rating = jdbcTemplate.query(sql, new MpaRatingMapper(), id).get(0);
            log.info("Found rating MPA {} by id {}.", rating.getName(), id);
            return Optional.of(rating);
        }
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> ratings = jdbcTemplate.query("SELECT * FROM mpa_rating ORDER BY mpa_rating_id",
                new MpaRatingMapper());
        log.info("Got all ratings MPA.");
        return ratings;
    }
}
