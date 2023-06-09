package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.InterfaceService.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;


    @Autowired
    public FilmServiceImpl(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                           @Qualifier("LikeDbStorage") LikeStorage likeStorage) {
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

    public Collection<Film> takeAll() {
        log.info("Запрос на получения списка фильмов.");
        return filmStorage.takeAll();
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