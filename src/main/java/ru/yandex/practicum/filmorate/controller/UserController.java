package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UsersService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UsersService usersService = new UsersService();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws Exception {
        usersService.create(user);
        return user;
    }

    @SneakyThrows
    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        usersService.update(user);
        return user;
    }

    @GetMapping
    public List<User> takeUsers() {
        return usersService.takeAll();
    }
}
