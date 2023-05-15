package ru.yandex.practicum.filmorate.service.InterfaceService;

import java.util.List;

public interface InterfaceServiceUser<T> {

    T create(T t);

    T update(T t);

    List<T> takeAll();


    T takeById(Integer id);

    void addFriend(Integer id, Integer friendId);

    void deleteFromFriend(Integer id, Integer friendId);

    List<T> takeFriends(Integer id);

    List<T> takeFriendsOfFriends(Integer id, Integer friendId);
}