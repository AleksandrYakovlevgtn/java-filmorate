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
import ru.yandex.practicum.filmorate.model.FriendShip;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.FriendshipStorage;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Repository
@Qualifier("FriendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int user1Id, int user2id) {
        try {
            if ((user1Id < 0 || user2id < 0) || user1Id == user2id) {
                throw new ExceptionsUpdate("Ошибка при создании дружбы в id:" + user1Id + " " + user2id);
            } else {
                String sql = "SELECT * FROM FRIENDSHIP WHERE (USER_1_ID = ? AND USER_2_ID = ?);";
                ResultSetWrappingSqlRowSet rs = (ResultSetWrappingSqlRowSet) jdbcTemplate.queryForRowSet(sql, user1Id, user2id);
                CachedRowSet set1 = (CachedRowSet) rs.getResultSet();
                ResultSetWrappingSqlRowSet rs2 = (ResultSetWrappingSqlRowSet) jdbcTemplate.queryForRowSet(sql, user2id, user1Id);
                CachedRowSet set2 = (CachedRowSet) rs2.getResultSet();
                if (set1.next()) {
                    confirmationFriend(user1Id, user2id);
                } else if (set2.next()) {
                    confirmationFriend(user2id, user1Id);
                } else {
                    String confirmSql = "INSERT INTO FRIENDSHIP(USER_1_ID, USER_2_ID, FRIENDSHIP_STATUS) VALUES(?, ?, ?);";
                    jdbcTemplate.update(confirmSql, user1Id, user2id, false);
                    log.info("Запрос в друзья оформлен.");
                }
            }
        } catch (DataAccessException | SQLException o) {
            throw new ExceptionsUpdate("При добавлении дружбы произошла ошибка.");
        }
    }

    @Override
    public void confirmationFriend(int user1Id, int user2id) {
        String sql = "UPDATE FRIENDSHIP SET FRIENDSHIP_STATUS = TRUE WHERE USER_1_ID = ? AND USER_2_ID = ?;";
        try {
            int update = jdbcTemplate.update(sql, user1Id, user2id);
            if (update > 0) {
                log.info("Дружба между пользователем с id: " + user1Id + " и пользователем с id: " + user2id + " подтверждена.");
            } else {
                log.info("Дружба между пользователем с id: " + user1Id + " и пользователем с id: " + user2id + " не подтверждена.");
            }
        } catch (DataAccessException o) {
            throw new ExceptionsUpdate("Дружба между пользователем с id: " + user1Id + " и пользователем с id: " + user2id + " не подтверждена из за ошибки");
        }
    }

    @Override
    public void deleteFriend(int user1Id, int user2id) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_1_ID = ? AND USER_2_ID = ?;";
        try {
            int update = jdbcTemplate.update(sql, user1Id, user2id);
            if (update > 0) {
                log.info("Дружба между пользователем с id: " + user1Id + " и пользователем с id: " + user2id + " удалена.");
            } else {
                log.info("Дружба между пользователем с id: " + user1Id + " и пользователем с id: " + user2id + " не удалена.");
            }
        } catch (DataAccessException o) {
            throw new ExceptionsUpdate("Дружба между пользователем с id: " + user1Id + " и пользователем с id: " + user2id + " не удалена из за ошибки");
        }
    }

    @Override
    public Set<FriendShip> takeFriendShip(int userId) {
        String sql = "SELECT * FROM FRIENDSHIP WHERE USER_1_ID = ? OR USER_2_ID = ?;";
        try {
            Collection<FriendShip> set = jdbcTemplate.query(sql, this::createFriendship, userId, userId);
            log.info("Получен список друзей пользователя");
            return new HashSet<>(set);
        } catch (EmptyResultDataAccessException o) {
            return new HashSet<>();
        }
    }

    @Override
    public Set<Integer> takeFriendsOfUser(int userId) {
        Set<FriendShip> set = takeFriendShip(userId);
        Set<Integer> friendsIds = new HashSet<>();
        if (set.isEmpty()) {
            log.info("У пользователя с id: " + userId + " нет друзей.");
        } else {
            for (FriendShip f : set) {
                if (f.getUser2Id() == userId && f.isStatus()) {
                    friendsIds.add(f.getUser1Id());
                } else if (f.getUser1Id() == userId) {
                    friendsIds.add(f.getUser2Id());
                }
            }
        }
        log.info("Получен список id друзей");
        return friendsIds;
    }

    public FriendShip createFriendship(ResultSet rs, int rowNum) throws SQLException {
        int user1Id = rs.getInt("USER_1_ID");
        int user2Id = rs.getInt("USER_2_ID");
        boolean status = rs.getBoolean("FRIENDSHIP_STATUS");
        return new FriendShip(user1Id, user2Id, status);
    }
}