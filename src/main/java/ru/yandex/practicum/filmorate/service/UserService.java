package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InterfaceService.InterfaceServiceUser;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Component
@Service
public class UserService implements InterfaceServiceUser<User> {
    private final InMemoryUserStorage dateUser;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(InMemoryUserStorage dateUser) {
        this.dateUser = dateUser;
    }

    public User create(User user) {
        if (dateUser.haveUser(user.getId())) {
            log.info("Пользователь уже существует!");
            return user;
        }
        log.info("Пользователь создан!");
        return dateUser.create(user);
    }

    public User update(User user) {
        if (dateUser.haveUserByEmail(user)) {
            throw new ExceptionsUpdate("Обновить не удалось, пользователь не существует");
        }
        log.info("Пользователь обновлен!");
        return dateUser.update(user);
    }

    public List<User> takeAll() {
        log.info("Получены все пользователи");
        return new ArrayList<>(dateUser.getUserBase().values());
    }

    public User takeById(Integer id) {
        if (!dateUser.haveUser(id)) {
            log.info("Пользователь не существует!");
            throw new ExceptionsUpdate("Пользователь не существует!");
        }
        log.info("Получен пользователь по id");
        return dateUser.takeById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        if ((!dateUser.haveUser(id) || (!dateUser.haveUser(friendId)))) {
            log.info("Пользователь не существует!");
            throw new ExceptionsUpdate("Пользователь не существует!");
        }
        User user = dateUser.takeById(id);
        User user2 = dateUser.takeById(friendId);
        user.setFriendsId(friendId);
        user2.setFriendsId(id);
        dateUser.update(user);
        dateUser.update(user2);
    }

    public void deleteFromFriend(Integer id, Integer friendId) {
        if ((!dateUser.haveUser(id)) || (!dateUser.haveUser(friendId))) {
            log.info("Пользователь не существует!");
            throw new ExceptionsUpdate("Пользователь не существует!");
        }
        User user = dateUser.takeById(id);
        user.getFriendsId().remove(friendId);
        dateUser.update(user);
    }

    public List<User> takeFriends(Integer id) {
        if (!dateUser.haveUser(id)) {
            log.info("Пользователь не существует!");
            throw new ExceptionsUpdate("Пользователь не существует!");
        }
        return dateUser.takeFriends(id);
    }

    public List<User> takeFriendsOfFriends(Integer id, Integer friendId) {
        if ((!dateUser.haveUser(id)) || (!dateUser.haveUser(friendId))) {
            log.info("Пользователь не существует!");
            throw new ExceptionsUpdate("Пользователь не существует!");
        }
        return dateUser.takeFriendsOfFriends(id, friendId);
    }
}