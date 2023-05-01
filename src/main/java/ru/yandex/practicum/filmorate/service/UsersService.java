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

    public User create(User user) throws Exception {
        if (date.haveUser(user)) {
            throw new Exception("Пользователь уже существует!");

        }
        return date.setUser(User.builder()
                .id(user.getId())
                .name(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .build());
    }

    public User update(User user) throws Exception {
        if (!date.haveFilmOrUser(user)) {
            throw new Exception("Обновить не удалось, пользователь не существует");
        }
        return date.setUser(User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .build());
    }

    public List<User> takeAll() {
        return new ArrayList<>(date.userBase.values());
    }
}
