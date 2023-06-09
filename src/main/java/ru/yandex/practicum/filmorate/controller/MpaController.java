package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaServiceImpl service;

    @Autowired
    public MpaController(MpaServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<Mpa> takeAll() {
        return service.takeAll();
    }

    @GetMapping("/{id}")
    public Mpa takeById(@PathVariable int id) {
        return service.takeById(id);
    }
}