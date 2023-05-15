package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmControllerTest {
    private Film film;
    protected InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    protected InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    protected FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
    protected UserService userService = new UserService(inMemoryUserStorage);
    private FilmController filmController = new FilmController(filmService,userService);
    private Validator val;

    @BeforeEach
    void beforeEach() {
        film = Film.builder()
                .id(1)
                .name("filmName")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(60)
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        val = factory.getValidator();
    }

    @Test
    void shouldCreateFilmWhenAllOk() {
        filmController.createFilm(film);
        assertEquals(film.getName(), filmController.takeFilms().get(0).getName());
    }

    @Test
    void shouldTakeViolationsWhenNameIsEmpty() {
        Film film1 = Film.builder()
                .id(2)
                .name("")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(60)
                .build();
        Set<ConstraintViolation<Film>> violations = val.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTakeViolationsWhenWrongDescription() {
        Film film1 = Film.builder()
                .id(2)
                .name("name")
                .description("qwertyuiop")
                .releaseDate(LocalDate.now())
                .duration(60)
                .build();
        for (int i = 0; i < 6; i++) {
            film1.setDescription(film1.getDescription() + film1.getDescription());
        }
        Set<ConstraintViolation<Film>> violations = val.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTakeViolationsWhenRealeseDateBeforeBerhsdayOfFilms() {
        Film film1 = Film.builder()
                .id(2)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1000, 1, 1))
                .duration(60)
                .build();
        Set<ConstraintViolation<Film>> violations = val.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTakeViolationsWhenDurationIsNegative() {
        Film film1 = Film.builder()
                .id(2)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> violations = val.validate(film1);
        assertFalse(violations.isEmpty());
    }
}