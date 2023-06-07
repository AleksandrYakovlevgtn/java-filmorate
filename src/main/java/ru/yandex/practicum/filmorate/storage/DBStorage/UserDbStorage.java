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
            throw new ExceptionsUpdate("Пользователи не найдены.");
        }
    }

    @Override
    public User takeById(Integer id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?;";
        try {
            User user = jdbcTemplate.queryForObject(sql, this::createUser, id);
            log.info("Получен пользователь по id: " + id);
            return user;
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("Не существует пользователя с id: " + id);
        }
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USERS(USER_EMAIL,USER_LOGIN, USER_NAME, USER_BIRTHDAY) VALUES(?, ?, ?, ?);";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName());
            }
            jdbcTemplate.update(con -> {
                PreparedStatement p = con.prepareStatement(sql, new String[]{"USER_ID"});
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
            throw new ExceptionsUpdate("Пользователь с id: " + user.getId() + " уже существует.");
        }
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?;";
        try {
            int update = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            if (update > 0) {
                log.info("Обновлен пользователь с id: " + user.getId());
                return user;
            } else {
                throw new ExceptionsUpdate("Пользователь не обнавлен.");
            }
        } catch (DuplicateKeyException o) {
            throw new ExceptionsUpdate("Обновление не прошло по причине ошибки.");
        }
    }

    @Override
    public boolean haveUserByEmail(User user) {
        return false;
    }

    @Override
    public boolean haveUser(Integer id) {
        boolean haveUser = false;
        String sql = "SELECT EXISTS(SELECT 1 FROM USERS WHERE USER_ID = ?);";
        try {
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, id);
            if (result != null) {
                haveUser = result;
            }
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("Пользователь не найден с таким id " + id);
        }
        return haveUser;
    }

    public User createUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("USER_ID");
        String email = rs.getString("USER_EMAIL");
        String login = rs.getString("USER_LOGIN");
        String name = rs.getString("USER_NAME");
        LocalDate birthday = rs.getDate("USER_BIRTHDAY").toLocalDate();
        Set<Integer> friends = friendshipStorage.takeFriendsOfUser(id);
        return new User(id, email, login, name, birthday, friends);
    }
}