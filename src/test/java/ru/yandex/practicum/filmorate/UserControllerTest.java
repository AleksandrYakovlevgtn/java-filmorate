package ru.yandex.practicum.filmorate;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class UserControllerTest {
    private User user;
    protected InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    protected UserService userService = new UserService(inMemoryUserStorage);
    private UserController userController = new UserController(userService);
    private Validator val;


    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1)
                .name("")
                .email("net@net.com")
                .login("net")
                .birthday(LocalDate.of(1988, 4, 7))
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        val = factory.getValidator();
    }

    @SneakyThrows
    @Test
    void shouldCreateUserWhenAllOk() {
        user.setName("oK");
        userController.createUser(user);
        assertEquals(user.getName(), userController.takeUsers().get(0).getName());
    }

    @Test
    void shouldTakeLoginWhenNameIsEmpty() {
        User user1 = User.builder()
                .id(1)
                .name(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName())
                .email("net@net.com")
                .login("net")
                .birthday(LocalDate.of(1988, 4, 7))
                .build();
        assertEquals("net", user1.getName());
    }

    @Test
    void shouldTakeViolationsWhenWrongEmail() {
        User user1 = User.builder()
                .id(1)
                .name("name")
                .email("abraKADABRA")
                .login("net")
                .birthday(LocalDate.of(1988, 4, 7))
                .build();
        Set<ConstraintViolation<User>> violations = val.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTakeViolationsWhenLoginWrong() {
        User user1 = User.builder()
                .id(1)
                .name("name")
                .email("net@net.com")
                .login("")
                .birthday(LocalDate.of(1988, 4, 7))
                .build();
        Set<ConstraintViolation<User>> violations = val.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTakeViolationsWhenBirthdayWrong() {
        User user1 = User.builder()
                .id(1)
                .name("name")
                .email("net@net.com")
                .login("net")
                .birthday(LocalDate.of(2088, 4, 7))
                .build();
        Set<ConstraintViolation<User>> violations = val.validate(user1);
        assertFalse(violations.isEmpty());
    }
}