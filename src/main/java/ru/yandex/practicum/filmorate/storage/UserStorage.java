package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> takeAll();

    User create(User user);

    User update(User user);

    User delete(int id);

    User takeById(Integer id);
    boolean haveUserByEmail(User user);
    boolean haveUser(Integer id);
}
