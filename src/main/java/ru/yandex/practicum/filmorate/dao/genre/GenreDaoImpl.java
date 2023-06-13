package ru.yandex.practicum.filmorate.dao.genre;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

@Slf4j
@Data
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> get(int id) {
        SqlRowSet ratingRow = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE genre_id=?", id);
        if (ratingRow.next()) {
            Genre genre = new Genre(ratingRow.getInt("genre_id"),
                    Objects.requireNonNull(ratingRow.getString("name")));
            log.info("Found genre {} by id {}.", id, genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Genre by id {} is not found.", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> get(String name) {
        SqlRowSet ratingRow = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE name=?", name);
        if (ratingRow.next()) {
            Genre genre = new Genre(ratingRow.getInt("genre_id"),
                    Objects.requireNonNull(ratingRow.getString("name")));
            log.info("Found genre {}.", genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Genre {} is not found.", name);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Genre> getAll() {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genre ORDER BY genre_id",
                new GenreMapper());
        log.info("Got all genres.");
        return genres;
    }
}
