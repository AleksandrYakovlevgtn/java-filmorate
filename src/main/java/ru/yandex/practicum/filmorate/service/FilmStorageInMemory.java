package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class FilmStorageInMemory {
    private HashMap<Integer, Film> filmBase = new HashMap<>();
    protected int filmId = 0;

    public int setFilmId() {
        return ++filmId;
    }

    public HashMap<Integer, Film> getFilmBase() {
        return filmBase;
    }

    public Film setFilm(Film film) {
        Integer id = film.getId();
        if (id == null || id == 0) {
            id = setFilmId();
            film.setId(id);
        }
        filmBase.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmBase.put(film.getId(), film);
        return film;
    }

    public boolean haveFilmByName(Film film) {
        boolean work = false;
        for (Map.Entry<Integer, Film> entry : filmBase.entrySet()) {
            if (entry.getValue().getName().equals(film.getName())) {
                work = true;
                break;
            }
        }
        return work;
    }

    public boolean haveFilm(Film film) {
        return filmBase.containsKey(film.getId());
    }
}