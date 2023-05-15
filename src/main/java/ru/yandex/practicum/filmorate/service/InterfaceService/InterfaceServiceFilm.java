package ru.yandex.practicum.filmorate.service.InterfaceService;

import java.util.List;

public interface InterfaceServiceFilm<T> {
    T create(T t);

    T update(T t) throws Exception;

    List takeAll();

    List takePopular(Integer count);

    T takeById(Integer id);

    void addLike(Integer id, Integer id2);

    void deleteLike(Integer id, Integer id2);
}