package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class FilmsService {
    @Autowired
    private DataBase date = new DataBase();

    public Film create(Film film)  {
        if (date.haveFilm(film)) {
            System.out.println("Фильм уже существует!");
            return film;
        }
        return date.setFilm(Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build());
    }

    public Film update(Film film) throws Exception {
        if (!date.haveFilmOrUser(film)) {
            System.out.println("Обновить не удалось, фильма не существует");
            return film;
        }
        return date.setFilm(Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build());
    }

    public List<Film> takeAll() {
        return new ArrayList<>(date.filmBase.values());
    }
}
