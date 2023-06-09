package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.InterfaceService.MpaService;
import ru.yandex.practicum.filmorate.storage.DBStorage.MpaDbStorage;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {
    private final MpaDbStorage storage;

    public MpaServiceImpl(MpaDbStorage storage) {
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