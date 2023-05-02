package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class DataBase {
    public HashMap<Integer, Film> filmBase = new HashMap<>();
    public HashMap<Integer, User> userBase = new HashMap<>();
    protected int userid = 0;
    protected int filmId = 0;

    public int setUserId() {
        userid++;
        return userid;
    }
    public int setFilmId(){
        filmId++;
        return filmId;
    }

    public Film setFilm(Film film) {
        Integer id = film.getId();
        if (id == null || id == 0) {
            id = setFilmId();
            film.setId(id);
        }
        filmBase.put(film.getId(), film);
        return film;
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

    public boolean haveFilmOrUser(Object someThing) {
        boolean work = false;
        if (someThing.getClass().equals(User.class)) {
            for (Map.Entry<Integer, User> entry : userBase.entrySet()) {
                if (entry.getValue().getEmail().equals(((User) someThing).getEmail())) {
                    work = true;
                    break;
                }
            }
        }
        if (someThing.getClass().equals(Film.class)) {
            for (Map.Entry<Integer, Film> entry : filmBase.entrySet()) {
                if (entry.getValue().getName().equals(((Film) someThing).getName())) {
                    work = true;
                    break;
                }
            }
        }
        return work;
    }

    public boolean haveUser(User user) {
        return userBase.containsKey(user.getId());
    }

    public boolean haveFilm(Film film) {
        return filmBase.containsKey(film.getId());
    }
}