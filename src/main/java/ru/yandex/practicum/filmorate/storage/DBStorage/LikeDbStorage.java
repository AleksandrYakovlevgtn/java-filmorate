package ru.yandex.practicum.filmorate.storage.DBStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.LikeStorage;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Repository
@Qualifier("LikeDbStorage")
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("UserDbStorage") UserDbStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public Set<Integer> takeLikes(int id) {
        Set<Integer> likes = new HashSet<>();
        try {
            if (haveFilm(id)) {
                String sql = "SELECT USER_ID FROM LIKES WHERE FILM_ID = ?;";
                ResultSetWrappingSqlRowSet set = (ResultSetWrappingSqlRowSet) jdbcTemplate.queryForRowSet(sql, id);
                CachedRowSet crsSet = (CachedRowSet) set.getResultSet();
                while (crsSet.next()) {
                    likes.add(set.getInt("USER_ID"));
                }
            }
        } catch (SQLException o) {
            throw new RuntimeException(o.getMessage());
        }
        log.info("У фильма с id: " + id + " получено likes: " + likes.size());
        return likes;
    }

    @Override
    public void addLike(int userId, int filmId) {
        String sql = "INSERT INTO LIKES(USER_ID, FILM_ID) VALUES (?, ?);";
        if (haveFilm(filmId)) {
            log.info("Добавление лайка. Проверка фильм существует.");
            if (userStorage.haveUser(userId)) {
                jdbcTemplate.update(sql, userId, filmId);
                log.info("Like фильму с id: " + filmId + " поставлен пользователем с id: " + userId);
            } else {
                log.info("Like не добавлен.");
            }
        } else {
            log.info("Like не добавлен.");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sql = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?;";
        try {
            if (userStorage.haveUser(userId) && haveFilm(filmId)) {
                jdbcTemplate.update(sql, userId, filmId);
                log.info("Like фильму с id: " + filmId + " пользователем с id: " + userId + " удален.");
            } else {
                throw new ExceptionsUpdate("Like не удален.");
            }
        } catch (DataAccessException o) {
            throw new ExceptionsUpdate("Like не удален.");
        }
    }

    public boolean haveFilm(Integer id) {
        String sql = " SELECT EXISTS(SELECT 1 FROM FILM WHERE ID = ?);";
        try {
            boolean resultOfHave = false;
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, id);
            if (result != null) {
                resultOfHave = result;
            }
            return resultOfHave;
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("При проверке на существования фильма в таблице получили exception.");
        }
    }
}