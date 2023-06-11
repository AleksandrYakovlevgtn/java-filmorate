package ru.yandex.practicum.filmorate.storage.interfaceStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> takeAll();

    Film create(Film film);

    Film update(Film film);

    Film takeById(Integer id);

    boolean haveFilm(Integer id);
}