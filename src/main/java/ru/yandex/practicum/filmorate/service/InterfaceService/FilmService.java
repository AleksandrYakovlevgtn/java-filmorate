package ru.yandex.practicum.filmorate.service.InterfaceService;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film) throws Exception;

    Collection takeAll();

    List takePopular(Integer count);

    Film takeById(Integer id);

    void addLike(Integer id, Integer id2);

    void deleteLike(Integer id, Integer id2);
}