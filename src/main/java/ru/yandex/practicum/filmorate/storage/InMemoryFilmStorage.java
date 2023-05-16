package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InterfaceStorage.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> filmBase = new HashMap<>();
    protected int filmId = 0;

    public int setFilmId() {
        return ++filmId;
    }

    public HashMap<Integer, Film> getFilmBase() {
        return filmBase;
    }

    public Film create(Film film) {
        Integer id = film.getId();
        if (id == null || id == 0) {
            id = setFilmId();
            film.setId(id);
        }
        filmBase.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        filmBase.put(film.getId(), film);
        return film;
    }

    public Collection<Film> takeAll() {
        return new ArrayList<>(filmBase.values());
    }

    public Film takeById(Integer id) {
        return filmBase.get(id);
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

    public boolean haveFilm(Integer id) {
        return filmBase.containsKey(id);
    }
}