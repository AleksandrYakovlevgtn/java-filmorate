package ru.yandex.practicum.filmorate.storage.DBStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Qualifier("GenreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> takeAll() {
        log.info("Получен список жанров.");
        String sql = "SELECT * FROM GENRE ORDER BY GENRE_ID;";
        return jdbcTemplate.query(sql, this::createGenre);
    }

    @Override
    public Genre takeById(int id) {
        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?;";
        try {
            log.info("Получен жанр с id:" + id);
            return jdbcTemplate.queryForObject(sql, this::createGenre, id);
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("Жанр не найден с таким id " + id);
        }
    }

    @Override
    public Set<Genre> takeGenreOfFilm(int filmId) {
        String sql = "SELECT G.GENRE_ID, G.GENRE_NAME" +
                " FROM GENRE_FILM AS GFILM " +
                "INNER JOIN GENRE AS G ON GFILM.GENRE_ID = G.GENRE_ID " +
                "WHERE GFILM.FILM_ID = ?;";
        try {
            Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql, this::createGenre, filmId));
            log.info("Получены жанры фильма с id: " + filmId);
            return genres;
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("Для фильма с id: " + filmId + " нет жанров");
        }
    }

    @Override
    public void addGenre(int genreId, int filmId) {
        String sql = "INSERT INTO GENRE_FILM(FILM_ID,GENRE_ID) VALUES(?, ?);";
        try {
            int update = jdbcTemplate.update(sql, filmId, genreId);
            if (update > 0) {
                log.info("Для фильма с id: " + filmId + " добавлен жанр с id: " + genreId);
            }
        } catch (DataAccessException o) {
            throw new ExceptionsUpdate("Жанры не добавлены");
        }
    }

    @Override
    public void deleteGenre(int genreId, int filmId) {
        String sql = "DELETE FROM GENRE_FILM WHERE FILM_ID = ?;";
        try {
            int delete = jdbcTemplate.update(sql, filmId);
            if (delete > 0) {
                log.info("Жанр с id: " + genreId + " удален у фильма с id: " + filmId);
            } else {
                log.info("Удаление жанра c id: " + genreId + " у фильма с id: " + filmId + " не прошло так как жанр или фильм не найдены");
            }
        } catch (DataAccessException o) {
            throw new ExceptionsUpdate("При удалении жанра что то пошло не так.");
        }
    }


    public Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}