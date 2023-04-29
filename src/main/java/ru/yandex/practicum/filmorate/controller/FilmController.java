package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    @Autowired
    private FilmsService filmsService = new FilmsService();

    @SneakyThrows
    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmsService.create(film);
    }

    @SneakyThrows
    @PutMapping
    public Film updeteFilm(@RequestBody @Valid Film film) {
        return filmsService.update(film);
    }

    @GetMapping
    public List<Film> takeFilms() {
        return filmsService.takeAll();
    }
}
