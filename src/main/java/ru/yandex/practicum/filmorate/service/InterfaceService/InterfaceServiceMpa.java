package ru.yandex.practicum.filmorate.service.InterfaceService;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface InterfaceServiceMpa {

    Mpa takeById(int id);

    List<Mpa> takeAll();
}