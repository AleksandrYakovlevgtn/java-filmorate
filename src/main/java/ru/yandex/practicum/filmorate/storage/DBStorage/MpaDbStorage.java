package ru.yandex.practicum.filmorate.storage.DBStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@Qualifier("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> takeAll() {
        try {
            String sql = "SELECT * FROM MPA ORDER BY ID;";
            List<Mpa> mpas = jdbcTemplate.query(sql, this::createMpa);
            log.info("Получен список MPA");
            return mpas;
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("MPA отсутствуют");
        }
    }

    @Override
    public Mpa takeById(int id) {
        try {
            String sql = "SELECT * FROM MPA WHERE ID = ?;";
            log.info("получен MPA по id: " + id);
            return jdbcTemplate.queryForObject(sql, this::createMpa, id);
        } catch (EmptyResultDataAccessException o) {
            throw new ExceptionsUpdate("MPA не найден с таким id: " + id);
        }
    }

    public Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("ID"), rs.getString("NAME"));
    }
}