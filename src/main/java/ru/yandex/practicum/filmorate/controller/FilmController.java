package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
@Component
public class FilmController {
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createFilm(@RequestBody @Valid Film film) {
        return ResponseEntity.ok(filmService.create(film));
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }


    @GetMapping
    public List<Film> takeFilms() {
        return filmService.takeAll();
    }

    @GetMapping("/{id}")
    public Film takeFilmById(@PathVariable Integer id) {
        return filmService.takeById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> addLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
        String message = "Поставлен like фильму " + filmService.takeById(id).getName() + " пользователем " + userService.takeById(userId).getName();
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Пользователь " + userService.takeById(userId).getName() + "удалил like фильму " + filmService.takeById(id).getName());
    }

    @GetMapping("/popular")
    public List<Film> takePopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.takePopular(count);
    }
}