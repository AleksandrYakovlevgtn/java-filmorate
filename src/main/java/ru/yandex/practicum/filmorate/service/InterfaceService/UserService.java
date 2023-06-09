package ru.yandex.practicum.filmorate.service.InterfaceService;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user);

    List<User> takeAll();


    User takeById(Integer id);

    void addFriend(Integer id, Integer friendId);

    void deleteFromFriend(Integer id, Integer friendId);

    List<User> takeFriends(Integer id);

    Collection<User> takeFriendsOfFriends(Integer id, Integer friendId);
}