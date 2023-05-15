package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.InterfaceService.InterfaceServiceFilm;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
public class FilmService implements InterfaceServiceFilm {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final InMemoryFilmStorage dateFilm;
    private final InMemoryUserStorage dateUser;

    @Autowired
    public FilmService(InMemoryFilmStorage dateFilm, InMemoryUserStorage dateUser) {
        this.dateFilm = dateFilm;
        this.dateUser = dateUser;
    }

    public Film create(Film film) {
        if (dateFilm.haveFilm(film.getId())) {
            log.error("Фильм уже существует!");
            return film;
        }
        log.info("Фильм создан!");
        return dateFilm.create(film);
    }

    public Film update(Film film) {
        if (!dateFilm.haveFilm(film.getId())) {
            throw new ExceptionsUpdate("Обновить не удалось, фильма не существует.");
        }
        return dateFilm.update(film);
    }

    public List<Film> takeAll() {
        log.info("Получены все фильмы");
        return new ArrayList<>(dateFilm.getFilmBase().values());
    }

    public List<Film> takePopular(Integer count) {
        log.info("Получен список популярных фильмов");
        return dateFilm.takeAll().stream()
                .sorted(Comparator.comparing(film -> -film.getLikes().size())).limit(count)
                .collect(Collectors.toList());
    }

    public Film takeById(Integer id) {
        if (!dateFilm.haveFilm(id)) {
            log.error("Фильм не существует!");
            throw new ExceptionsUpdate("Пользователь не существует!");
        }
        log.info("Получен фильм по id");
        return dateFilm.takeById(id);
    }

    public void addLike(Integer id, Integer userID) {
        if (dateFilm.haveFilm(id) && dateUser.haveUser(userID)) {
            dateFilm.takeById(id).getLikes().add(id);
            log.info("Поставлен like фильму " + dateFilm.takeById(id).getName());
            dateFilm.update(dateFilm.takeById(id));
        } else {
            log.error("При попытке поставить like произошла ошибка, пользователь или фильм не найден.");
            throw new ExceptionsUpdate("При попытке поставить like произошла ошибка, пользователь или фильм не найден.");
        }
    }

    public void deleteLike(Integer id, Integer userID) {
        if (dateFilm.haveFilm(id) && dateUser.haveUser(userID)) {
            dateFilm.takeById(id).getLikes().remove(id);
            log.info("Удален like фильму " + dateFilm.takeById(id).getName());
            dateFilm.update(dateFilm.takeById(id));
        } else {
            log.error("При попытке удалить like произошла ошибка, пользователь или фильм не найден.");
            throw new ExceptionsUpdate("При попытке удалить like произошла ошибка, пользователь или фильм не найден.");
        }
    }
}