package ru.yandex.practicum.filmorate.storage.interfaceStorage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> takeAll();

    Genre takeById(int id);

    Set<Genre> takeGenreOfFilm(int filmId);

    void addGenre(int genreId, int filmId);

    void deleteGenre(int genreId, int filmId);
}