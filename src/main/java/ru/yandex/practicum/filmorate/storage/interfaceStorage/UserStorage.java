package ru.yandex.practicum.filmorate.storage.interfaceStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> takeAll();

    User create(User user);

    User update(User user);

    User takeById(Integer id);

    boolean haveUser(Integer id);
}