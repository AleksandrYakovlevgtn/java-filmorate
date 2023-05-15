package ru.yandex.practicum.filmorate.service.InterfaceService;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface InterfaceServiceFilm {
    Film create(Film film);

    Film update(Film film) throws Exception;

    List takeAll();

    List takePopular(Integer count);

    Film takeById(Integer id);

    void addLike(Integer id, Integer id2);

    void deleteLike(Integer id, Integer id2);
}