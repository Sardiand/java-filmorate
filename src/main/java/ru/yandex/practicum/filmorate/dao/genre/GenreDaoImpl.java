package ru.yandex.practicum.filmorate.dao.genre;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

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
        String sql = "SELECT * FROM genre WHERE genre_id=?";
        if (jdbcTemplate.query(sql, new GenreMapper(), id).isEmpty()) {
            log.info("Genre by id {} is not found.", id);
            return Optional.empty();
        } else {
            Genre genre = jdbcTemplate.query(sql, new GenreMapper(), id).get(0);
            log.info("Found genre {} by id {}.", id, genre.getName());
            return Optional.of(genre);
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genre ORDER BY genre_id",
                new GenreMapper());
        log.info("Got all genres.");
        return genres;
    }
}
