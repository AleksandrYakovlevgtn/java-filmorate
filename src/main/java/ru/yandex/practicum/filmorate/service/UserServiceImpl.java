package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InterfaceService.ServiceUser;
import ru.yandex.practicum.filmorate.storage.DBStorage.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.DBStorage.UserDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements ServiceUser {
    private final UserDbStorage userStorage;
    private final FriendshipDbStorage friendStorage;

    @Autowired
    public UserServiceImpl(@Qualifier("UserDbStorage") UserDbStorage userStorage,
                           @Qualifier("FriendshipDbStorage") FriendshipDbStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User create(User user) {
        log.info("Получен запрос на создание пользователя!");
        return userStorage.create(user);
    }

    public User update(User user) {
        log.info("Получен запрос на обновление пользователя!");
        return userStorage.update(user);
    }

    public List<User> takeAll() {
        log.info("Получены запрос на получения списка пользователей.");
        return new ArrayList<>(userStorage.takeAll());
    }

    public User takeById(Integer id) {
        log.info("Получен запрос на получение пользователя по id");
        return userStorage.takeById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        log.info("Получен запрос на добавления в друзья.");
        friendStorage.addFriend(id, friendId);
    }

    public void deleteFromFriend(Integer id, Integer friendId) {
        log.info("Получен запрос на удаление из друзья.");
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> takeFriends(Integer id) {
        List<User> users = new ArrayList<>();
        User user = userStorage.takeById(id);
        for (Integer friendsIds : user.getFriendsId()) {
            users.add(userStorage.takeById(friendsIds));
        }
        log.info("Получен список друзей пользователя.");
        return users;
    }

    public Collection<User> takeFriendsOfFriends(Integer id, Integer friendId) {
        User user1 = userStorage.takeById(id);
        User user2 = userStorage.takeById(friendId);
        if (user1.getFriendsId() == null && user2.getFriendsId() == null) {
            return new HashSet<>();
        } else {
            Set<User> users = new HashSet<>();
            Set<Integer> confirmUsers = user1.getFriendsId()
                    .stream()
                    .filter(user2.getFriendsId()::contains)
                    .collect(Collectors.toSet());
            for (Integer comfirmIds : confirmUsers) {
                users.add(userStorage.takeById(comfirmIds));
            }
            return users;
        }
    }
}