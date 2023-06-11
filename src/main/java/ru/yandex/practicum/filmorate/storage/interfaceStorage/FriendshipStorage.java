package ru.yandex.practicum.filmorate.storage.interfaceStorage;

import ru.yandex.practicum.filmorate.model.FriendShip;

import java.util.Set;

public interface FriendshipStorage {

    void addFriend(int user1Id, int user2id);

    void confirmationFriend(int user1Id, int user2id);

    void deleteFriend(int user1Id, int user2id);

    Set<FriendShip> takeFriendShip(int userId);

    Set<Integer> takeFriendsOfUser(int userId);
}