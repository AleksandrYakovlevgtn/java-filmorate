package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreServiceImpl service;

    @Autowired
    public GenreController(GenreServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<Genre> takeAll() {
        return service.takeAll();
    }

    @GetMapping("/{id}")
    public Genre takeById(@PathVariable int id) {
        return service.takeById(id);
    }
}