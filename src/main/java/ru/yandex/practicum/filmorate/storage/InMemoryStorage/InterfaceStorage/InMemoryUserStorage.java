package ru.yandex.practicum.filmorate.storage.InMemoryStorage.InterfaceStorage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Integer, User> userBase = new HashMap<>();
    private int userid = 0;

    private int setUserId() {
        return ++userid;
    }

    public HashMap<Integer, User> getUserBase() {
        return userBase;
    }

    public User create(User user) {
        Integer id = user.getId();
        String name = user.getName();
        if (id == null || id == 0) {
            id = setUserId();
            user.setId(id);
        }
        if (name == null || name.isBlank()) {
            user.setName(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName());
        }
        userBase.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        String name = user.getName();
        if (name == null) {
            user.setName(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName());
        }
        userBase.put(user.getId(), user);
        return user;
    }

    public Collection<User> takeAll() {
        return new ArrayList<>(userBase.values());
    }

    public User takeById(Integer id) {
        return userBase.get(id);
    }

    public List<User> takeFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendsId : takeById(id).getFriendsId()) {
            if (takeById(friendsId) == null) {
                throw new ExceptionsUpdate("Друг не существует.");
            }
            if (friends.contains(friendsId)) {
                break;
            }
            friends.add(takeById(friendsId));
        }
        return friends;
    }

    public List<User> takeFriendsOfFriends(Integer id1, Integer friendId) {
        List<User> friends = new ArrayList<>();
        User user = takeById(id1);
        User userFriend = takeById(friendId);
        if (!user.getFriendsId().isEmpty() || !userFriend.getFriendsId().isEmpty()) {
            for (Integer numbers : user.getFriendsId()) {
                if (userFriend.getFriendsId().contains(numbers)) {
                    friends.add(userBase.get(numbers));
                }
            }
        }
        return friends;
    }

    public boolean haveUserByEmail(User user) {
        boolean work = false;
        for (Map.Entry<Integer, User> entry : userBase.entrySet()) {
            if (entry.getValue().getEmail().equals(user.getEmail())) {
                work = true;
                break;
            }
        }
        return work;
    }

    public boolean haveUser(Integer id) {
        return userBase.containsKey(id);
    }
}