package ru.yandex.practicum.filmorate.storage.interfaceStorage;

import java.util.Set;

public interface LikeStorage {
    Set<Integer> takeLikes(int id);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);
}