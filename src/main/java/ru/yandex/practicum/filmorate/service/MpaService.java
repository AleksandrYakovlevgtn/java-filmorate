package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.InterfaceService.InterfaceServiceMpa;
import ru.yandex.practicum.filmorate.storage.DBStorage.MpaDbStorage;

import java.util.List;

@Service
public class MpaService implements InterfaceServiceMpa {
    private final MpaDbStorage storage;

    @Autowired
    public MpaService(MpaDbStorage storage) {
        this.storage = storage;
    }

    @Override
    public Mpa takeById(int id) {
        return storage.takeById(id);
    }

    @Override
    public List<Mpa> takeAll() {
        return storage.takeAll();
    }
}