package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface InterfaceService<T> {
    T create(T t);

    T update(T t) throws Exception;

    List takeAll();
}