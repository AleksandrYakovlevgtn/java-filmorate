package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class DataBase {
    public HashMap<Integer, Film> filmBase = new HashMap<>();
    public HashMap<Integer, User> userBase = new HashMap<>();
    protected int id = 0;

    public int setId() {
        id++;
        return id;
    }

    public Film setFilm(Film film) {
        Film work = film;
        if (film.getId() == 0) {
            int workID = setId();
            work = Film.builder()
                    .id(workID)
                    .description(film.getDescription())
                    .duration(film.getDuration())
                    .name(film.getName())
                    .releaseDate(film.getReleaseDate())
                    .build();
            filmBase.put(workID, work);
        } else {
            filmBase.put(film.getId(), film);
        }
        return work;
    }

    public User setUser(User user) {
        User work = user;
        if (user.getId() == 0) {
            int workID = setId();
            work = User.builder()
                    .id(workID)
                    .name(user.getName())
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .birthday(user.getBirthday())
                    .build();
            userBase.put(workID, work);
        } else {
            userBase.put(user.getId(), user);
        }
        return work;
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