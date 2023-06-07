package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.InterfaceService.InterfaceServiceFilm;
import ru.yandex.practicum.filmorate.storage.DBStorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.DBStorage.LikeDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService implements InterfaceServiceFilm {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmDbStorage filmStorage;
    private final LikeDbStorage likeStorage;


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmDbStorage filmStorage,
                       @Qualifier("LikeDbStorage") LikeDbStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public Film create(Film film) {
        log.info("Запрос на создание фильма:" + film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.info("Запрос на обновление фильма:" + film.getId());
        return filmStorage.update(film);
    }

    public List<Film> takeAll() {
        log.info("Запрос на получения списка фильмов.");
        return new ArrayList<>(filmStorage.takeAll());
    }

    public List<Film> takePopular(Integer count) {
        log.info("Получен список популярных фильмов.");
        return filmStorage.takeAll().stream()
                .sorted(Comparator.comparing(film -> -film.getLikes().size())).limit(count)
                .collect(Collectors.toList());
    }

    public Film takeById(Integer id) {
        log.info("Запрос на получения фильма по id: " + id);
        return filmStorage.takeById(id);
    }

    public void addLike(Integer userId, Integer filmId) {
        log.info("Запрос на добавление лайка.");
        likeStorage.addLike(userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userID) {
        log.info("Запрос на удаление лайка.");
        likeStorage.deleteLike(userID, filmId);
    }
}