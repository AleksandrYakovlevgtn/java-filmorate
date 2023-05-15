package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> takeAll();

    Film create(Film film);

    Film update(Film film);

    Film delete(int id);

    Film takeById(Integer id);

    boolean haveFilmByName(Film film);

    boolean haveFilm(Integer id);
}
