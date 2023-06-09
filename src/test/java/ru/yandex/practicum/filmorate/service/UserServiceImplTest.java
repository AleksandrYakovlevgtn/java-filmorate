package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DBStorage.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.DBStorage.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserDbStorage userStorage;
    private final FriendshipDbStorage friendStorage;
    User user = new User(1, "not@not.com", "login", "name", LocalDate.now(), new HashSet<>());
    User user2 = new User(2, "not@nao.com", "login", "friend", LocalDate.now(), new HashSet<>());

    @Test
    void create() {
        userStorage.create(user);
        assertEquals(user.getName(), userStorage.takeById(1).getName());
    }

    @Test
    void update() {
        user.setName("nameTest");
        userStorage.update(user);
        assertEquals("nameTest", userStorage.takeById(1).getName());
    }

    @Test
    void addFriend() {
        userStorage.create(user2);
        friendStorage.addFriend(user.getId(), user2.getId());
        assertEquals(friendStorage.takeFriendsOfUser(user.getId()).size(), 1);
    }

    @Test
    void deleteFromFriend() {
        friendStorage.deleteFriend(user.getId(), user2.getId());
        assertEquals(friendStorage.takeFriendsOfUser(user.getId()).size(), 0);
    }
}