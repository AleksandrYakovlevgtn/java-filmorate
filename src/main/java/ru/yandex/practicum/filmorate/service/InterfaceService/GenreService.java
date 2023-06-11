package ru.yandex.practicum.filmorate.service.InterfaceService;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> takeAll();

    Genre takeById(int id);
}