package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

@Slf4j
@Data
@Component
public class MpaRatingDaoImpl implements MpaRatingDao{
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MpaRating> get(int id) {
        SqlRowSet ratingRow = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating WHERE mpa_rating_id=?", id);
        if (ratingRow.next()) {
            MpaRating rating = new MpaRating(ratingRow.getInt("mpa_rating_id"),
                    Objects.requireNonNull(ratingRow.getString("rating")));
            log.info("Found rating MPA {} by id {}.", rating.getName(), id);
            return Optional.of(rating);
        } else {
            log.info("Rating MPA by id {} is not found.", id);
            return Optional.empty();
        }
    }

    @Override
    public Collection<MpaRating> getAll() {
        List<MpaRating> ratings = jdbcTemplate.query("SELECT * FROM mpa_rating ORDER BY mpa_rating_id",
                new MpaRatingMapper());
        log.info("Got all ratings MPA.");
        return ratings;
    }
}
