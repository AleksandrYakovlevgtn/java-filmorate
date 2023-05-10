package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UsersService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
@Component
public class UserController {
    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws Exception {
        return usersService.create(user);
    }

    @SneakyThrows
    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return usersService.update(user);
    }

    @GetMapping
    public List<User> takeUsers() {
        return usersService.takeAll();
    }
}