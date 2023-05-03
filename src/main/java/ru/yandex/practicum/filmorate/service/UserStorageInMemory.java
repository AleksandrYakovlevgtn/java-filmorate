package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserStorageInMemory {

    private HashMap<Integer, User> userBase = new HashMap<>();
    protected int userid = 0;

    public int setUserId() {
        return ++userid;
    }

    public HashMap<Integer, User> getUserBase() {
        return userBase;
    }

    public User setUser(User user) {
        Integer id = user.getId();
        if (id == null || id == 0) {
            id = setUserId();
            user.setId(id);
        }
        userBase.put(user.getId(), user);
        return user;
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

    public boolean haveUser(User user) {
        return userBase.containsKey(user.getId());
    }
}