package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DBStorage.FilmDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceImplTest {
    Integer duracation = 120;
    LocalDate date = LocalDate.now();
    Set<Genre> genres = new HashSet<>();
    Set<Integer> likes = new HashSet<>();
    Mpa mpa = new Mpa(1);
    Film film = new Film(1, "film", "description", date, duracation, mpa, genres, likes);
    private final FilmDbStorage filmStorage;

    @Test
    void create() {
        filmStorage.create(film);
        assertEquals("Фильм создан", film.getName(), filmStorage.takeById(1).getName());
    }

    @Test
    void update() {
        film.setName("FilmupdateName");
        filmStorage.update(film);
        assertEquals("Обновление фильма", film.getName(), filmStorage.takeById(1).getName());
    }

    @Test
    void takeById() {
        assertEquals("Получение по id", film.getDescription(), filmStorage.takeById(1).getDescription());
    }
}