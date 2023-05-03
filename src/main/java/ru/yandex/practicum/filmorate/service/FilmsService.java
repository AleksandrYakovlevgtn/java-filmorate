package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component
public class FilmsService implements InterfaceService<Film> {
    private static final Logger log = LoggerFactory.getLogger(FilmsService.class);

    private FilmStorageInMemory date = new FilmStorageInMemory();

    public Film create(Film film) {
        if (date.haveFilm(film)) {
            log.info("Фильм уже существует!");
            return film;
        }
        log.info("Фильм создан!");
        return date.setFilm(Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build());
    }

    public Film update(Film film) throws Exception {
        if (date.haveFilmByName(film)) {
            throw new Exception("Обновить не удалось, фильма не существует");
        }
        log.info("Фильм обновлен!");
        return date.setFilm(Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build());
    }

    public List<Film> takeAll() {
        log.info("Получены все фильмы");
        return new ArrayList<>(date.getFilmBase().values());
    }
}