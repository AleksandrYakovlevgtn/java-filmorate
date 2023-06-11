package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmServiceImpl filmService;
    private final UserServiceImpl userService;

    @Autowired
    public FilmController(FilmServiceImpl filmService, UserServiceImpl userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {
        return ResponseEntity.ok(filmService.create(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film film) {
        return ResponseEntity.ok(filmService.update(film));
    }

    @GetMapping
    public Collection<Film> takeFilms() {
        return filmService.takeAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> takeFilmById(@PathVariable Integer id) {
        return ResponseEntity.ok(filmService.takeById(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> takePopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.takePopular(count);
    }
}