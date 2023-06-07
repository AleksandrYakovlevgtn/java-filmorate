package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.InterfaceService.InterfaceServiceGenre;
import ru.yandex.practicum.filmorate.storage.DBStorage.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService implements InterfaceServiceGenre {
    GenreDbStorage storage;

    @Autowired
    public GenreService(GenreDbStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Genre> takeAll() {
        return new ArrayList<>(storage.takeAll());
    }

    @Override
    public Genre takeById(int id) {
        return storage.takeById(id);
    }
}