package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UsersService implements InterfaceService<User> {
    private UserStorageInMemory date = new UserStorageInMemory();
    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    public User create(User user) {
        if (date.haveUser(user)) {
            log.info("Пользователь уже существует!");
            return user;
        }
        log.info("Фильм создан!");
        return date.setUser(User.builder()
                .id(user.getId())
                .name(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .build());
    }

    public User update(User user) throws Exception {
        if (date.haveUserByEmail(user)) {
            throw new Exception("Обновить не удалось, пользователь не существует");
        }
        log.info("Пользователь обновлен!");
        return date.setUser(User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .build());
    }

    public List<User> takeAll() {
        log.info("Получены все пользователи");
        return new ArrayList<>(date.getUserBase().values());
    }
}