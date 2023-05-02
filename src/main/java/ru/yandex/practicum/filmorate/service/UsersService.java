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

    public User create(User user) {
        if (date.haveUser(user)) {
            System.out.println("Пользователь уже существует!");
            return user;
        }
        User user2 = User.builder()
                .id(user.getId())
                .name(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .build();
        date.setUser(user2);
        return user2;
    }

    public User update(User user) throws Exception {
        User user2 = null;
        if (date.haveFilmOrUser(user)) {
            throw new Exception("Обновить не удалось, пользователь не существует");
        }
        if (!date.haveFilmOrUser(user)) {
            user2 = date.setUser(User.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .birthday(user.getBirthday())
                    .build());
        }
        return user2;
    }

    public List<User> takeAll() {
        return new ArrayList<>(date.userBase.values());
    }
}
