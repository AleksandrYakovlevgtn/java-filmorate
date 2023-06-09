package ru.yandex.practicum.filmorate.storage.DBStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("GenreDbStorage") GenreStorage genreStorage,
                         @Qualifier("MpaDbStorage") MpaStorage mpaStorage,
                         @Qualifier("LikeDbStorage") LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public Collection<Film> takeAll() {
        try {
            String sql = "SELECT * FROM FILM;";
            Collection<Film> films = jdbcTemplate.query(sql, this::createFilm);
            log.info("Получен список фильмов.");
            return films;
        } catch (EmptyResultDataAccessException o) {
            log.error("При получении списка фильмов получили exception",o.getMessage());
            throw new ExceptionsUpdate("Список фильмов пуст.");
        }
    }

    @Override
    public Film takeById(Integer id) {
        String sql = "SELECT * FROM FILM WHERE ID = ?;";
        try {
            Film film = jdbcTemplate.queryForObject(sql, this::createFilm, id);
            log.info("Получен фильм с id: " + id);
            return film;
        } catch (DataRetrievalFailureException o) {
            log.error("При получении Фильма с id: " + id + "  получили exception",o.getMessage());
            throw new ExceptionsUpdate("Фильма с id: " + id + " в таблице нет.");
        }
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILM(NAME, DESCRIPTION," +
                " RELEASE_DATE, DURATION, MPA_ID)" +
                " VALUES(?, ?, ?, ?, ?);";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement pr = connection.prepareStatement(sql, new String[]{"ID"});
                pr.setString(1, film.getName());
                pr.setString(2, film.getDescription());
                pr.setDate(3, Date.valueOf(film.getReleaseDate()));
                pr.setInt(4, film.getDuration());
                pr.setInt(5, film.getMpa().getId());
                return pr;
            }, keyHolder);
            log.info("Фильм добавлен в таблицу FILM");
            film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            if (Optional.ofNullable(film.getGenres()).isPresent()) {
                log.info("Создание. отправлено на добавление жанров.");
                for (Genre genre : film.getGenres()) {
                    genreStorage.addGenre(genre.getId(), film.getId());
                }
            }
            return takeById(film.getId());
        } catch (DuplicateKeyException o) {
            log.error("При создании фильма получили exception",o.getMessage());
            throw new ExceptionsUpdate("Фильм уже существует");
        }
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE ID = ?;";
        try {
            Film workFilm = takeById(film.getId());
            if (Optional.ofNullable(workFilm.getGenres()).isPresent()) {
                log.info("Обновление. отправляем на удаление жанров.");
                Set<Genre> workGenre1 = new HashSet<>(workFilm.getGenres());
                for (Genre genre : workGenre1) {
                    genreStorage.deleteGenre(genre.getId(), film.getId());
                }
            }
            Optional<Set<Genre>> genres = Optional.ofNullable(film.getGenres());
            if (genres.isPresent()) {
                log.info("Обновление. отправляем на добавление жанров.");
                Set<Genre> workGenre2 = new HashSet<>(genres.get());
                for (Genre genre : workGenre2) {
                    genreStorage.addGenre(genre.getId(), film.getId());
                }
            }
            int update = jdbcTemplate.update(sql, film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            if (update > 0) {
                log.info("Фильм с id: " + film.getId() + " обновлен.");
            }
            return takeById(film.getId());
        } catch (EmptyResultDataAccessException o) {
            log.error("При обновлении фильма  получили exception",o.getMessage());
            throw new ExceptionsUpdate("При обновлении фильма произошла ошибка.");
        }
    }

    @Override
    public boolean haveFilm(Integer id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM FILM WHERE ID = ?);";
        try {
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, id);
            if (result != null) {
                return result;
            } else {
            return false;
            }
        } catch (EmptyResultDataAccessException o) {
            log.error("При проверке на существования фильма в таблице получили exception.");
            throw new ExceptionsUpdate("При проверке на существования фильма в таблице получили exception.");
        }
    }

    public Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        int mpaId = rs.getInt("MPA_ID");

        Mpa mpa = mpaStorage.takeById(mpaId);
        Set<Genre> genres = genreStorage.takeGenreOfFilm(id);
        Set<Integer> likes = likeStorage.takeLikes(id);
        return new Film(id, name, description, releaseDate, duration, mpa, genres, likes);
    }
}