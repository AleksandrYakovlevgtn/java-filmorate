package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UsersService implements InterfaceService<User> {
    private final UserStorageInMemory date;
    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    public UsersService(UserStorageInMemory date) {
        this.date = date;
    }

    public User create(User user) {
        if (date.haveUser(user)) {
            log.info("Пользователь уже существует!");
            return user;
        }
        log.info("Фильм создан!");
        return date.setUser(user);
    }

    public User update(User user) throws Exception {
        if (date.haveUserByEmail(user)) {
            throw new ExceptionsUpdate("Обновить не удалось, пользователь не существует");
        }
        log.info("Пользователь обновлен!");
        return date.updateUser(user);
    }

    public List<User> takeAll() {
        log.info("Получены все пользователи");
        return new ArrayList<>(date.getUserBase().values());
    }
}