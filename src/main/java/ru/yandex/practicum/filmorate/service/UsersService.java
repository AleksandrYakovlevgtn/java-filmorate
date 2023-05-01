package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UsersService {
    @Autowired
    private DataBase date = new DataBase();

    public void create(User user) {
        if (date.haveUser(user)) {
            System.out.println("Пользователь уже существует!");

        }
        date.setUser(User.builder()
                .id(user.getId())
                .name(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .build());
    }

    public void update(User user) {
        if (date.haveFilmOrUser(user)) {
            System.out.println("Обновить не удалось, пользователь не существует");
        }
        if (!date.haveFilmOrUser(user)) {
            date.setUser(User.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .birthday(user.getBirthday())
                    .build());
        }
    }

    public List<User> takeAll() {
        return new ArrayList<>(date.userBase.values());
    }
}
