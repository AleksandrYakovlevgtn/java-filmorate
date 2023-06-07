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

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final LikeDbStorage likeStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("GenreDbStorage") GenreDbStorage genreStorage,
                         @Qualifier("MpaDbStorage") MpaDbStorage mpaStorage,
                         @Qualifier("LikeDbStorage") LikeDbStorage likeStorage) {
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
            throw new ExceptionsUpdate("Список фильмов пуст.");
        }
    }

    @Override
    public Film takeById(Integer id) {
        String sql = "SELECT * FROM FILM WHERE FILM_ID = ?;";
        try {
            Film film = jdbcTemplate.queryForObject(sql, this::createFilm, id);
            log.info("Получен фильм с id: " + id);
            return film;
        } catch (DataRetrievalFailureException o) {
            throw new ExceptionsUpdate("Фильма с id: " + id + " в таблице нет.");
        }
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILM(FILM_NAME, FILM_DESCRIPTION," +
                " FILM_RELEASE_DATE, FILM_DURATION, FILM_MPA_ID)" +
                " VALUES(?, ?, ?, ?, ?);";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement pr = connection.prepareStatement(sql, new String[]{"FILM_ID"});
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
            throw new ExceptionsUpdate("Фильм уже существует");
        }
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM SET FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?, FILM_DURATION = ?, FILM_MPA_ID = ? WHERE FILM_ID = ?;";
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
            throw new ExceptionsUpdate("При обновлении фильма произошла ошибка.");
        }
    }

    @Override
    public boolean haveFilmByName(Film film) {
        return false;
    }

    @Override
    public boolean haveFilm(Integer id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM FILM WHERE FILM_ID = ?);";
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

    public Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("FILM_DESCRIPTION");
        LocalDate releaseDate = rs.getDate("FILM_RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("FILM_DURATION");
        int mpaId = rs.getInt("FILM_MPA_ID");

        Mpa mpa = mpaStorage.takeById(mpaId);
        Set<Genre> genres = genreStorage.takeGenreOfFilm(id);
        Set<Integer> likes = likeStorage.takeLikes(id);
        return new Film(id, name, description, releaseDate, duration, mpa, genres, likes);
    }
}