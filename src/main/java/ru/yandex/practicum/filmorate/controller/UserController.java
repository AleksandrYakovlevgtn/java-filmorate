package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFromFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> takeFriends(@PathVariable Integer id) {
        return userService.takeFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> takeFriendsOfFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.takeFriendsOfFriends(id, otherId);
    }

    @GetMapping
    public List<User> takeUsers() {
        return userService.takeAll();
    }

    @GetMapping("/{id}")
    public User takeUserById(@PathVariable Integer id) {
        return userService.takeById(id);
    }
}