package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.InterfaceService.GenreService;
import ru.yandex.practicum.filmorate.storage.DBStorage.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDbStorage storage;

    public GenreServiceImpl(GenreDbStorage storage) {
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