package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
@Component
public class FilmController {
    private final FilmsService filmsService;

    @Autowired
    public FilmController(FilmsService filmsService) {
        this.filmsService = filmsService;
    }

    @SneakyThrows
    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmsService.create(film);
    }

    @SneakyThrows
    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmsService.update(film);
    }

    @GetMapping
    public List<Film> takeFilms() {
        return filmsService.takeAll();
    }
}