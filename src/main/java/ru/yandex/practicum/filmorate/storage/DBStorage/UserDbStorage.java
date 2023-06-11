package ru.yandex.practicum.filmorate.storage.DBStorage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Repository
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipDbStorage friendshipStorage;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("FriendshipDbStorage") FriendshipDbStorage friendshipStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public Collection<User> takeAll() {
        String sql = "SELECT * FROM USERS";
        try {
            Collection<User> users = jdbcTemplate.query(sql, this::createUser);
            log.info("Получен список пользователей.");
            return users;
        } catch (EmptyResultDataAccessException o) {
            log.error("При получении списка пользователей получили exception,", o.getMessage());
            throw new ExceptionsUpdate("Пользователи не найдены.");
        }
    }

    @Override
    public User takeById(Integer id) {
        String sql = "SELECT * FROM USERS WHERE ID = ?;";
        try {
            User user = jdbcTemplate.queryForObject(sql, this::createUser, id);
            log.info("Получен пользователь по id: " + id);
            return user;
        } catch (EmptyResultDataAccessException o) {
            log.error("При получении пользователя с id: " + id + " получили exception", o.getMessage());
            throw new ExceptionsUpdate("Не существует пользователя с id: " + id);
        }
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?);";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName());
            }
            jdbcTemplate.update(con -> {
                PreparedStatement p = con.prepareStatement(sql, new String[]{"ID"});
                p.setString(1, user.getEmail());
                p.setString(2, user.getLogin());
                p.setString(3, user.getName());
                p.setDate(4, Date.valueOf(user.getBirthday()));
                return p;
            }, keyHolder);
            user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            log.info("Добавили в таблицу пользователя с id: " + user.getId());
            return user;
        } catch (DuplicateKeyException o) {
            log.error("При создании пользователя с id: " + user.getId() + " получили exception так как пользователь существует.", o.getMessage());
            throw new ExceptionsUpdate("Пользователь с id: " + user.getId() + " уже существует.");
        }
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?;";
        try {
            int update = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            if (update > 0) {
                log.info("Обновлен пользователь с id: " + user.getId());
                return user;
            } else {
                throw new ExceptionsUpdate("Пользователь не обновлен.");
            }
        } catch (DuplicateKeyException o) {
            log.error("При оновлении пользователя  получили exception.", o.getMessage());
            throw new ExceptionsUpdate("Обновление не прошло по причине ошибки.");
        }
    }

    @Override
    public boolean haveUser(Integer id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM USERS WHERE ID = ?);";
        try {
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, id);
            if (result != null) {
                return result;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException o) {
            log.error("При проверке на существование пользователя с id: " + id + " получили exception", o.getMessage());
            throw new ExceptionsUpdate("Пользователь не найден с таким id " + id);
        }
    }

    public User createUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        Set<Integer> friends = friendshipStorage.takeFriendsOfUser(id);
        return new User(id, email, login, name, birthday, friends);
    }
}