package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    int id;
    @NotBlank(message = "электронная почта не может быть пустой.")
    @Email(message = "должна содержать символ @.")
    String email;
    @NotBlank(message = "логин не может быть пустым и содержать пробелы.")
    String login;
    String name;
    @Past(message = "дата рождения не может быть в будущем.")
    LocalDate birthday;

    private final Set<Integer> friendsId = new HashSet<>();

    public void setFriendsId(Integer id) {
        friendsId.add(id);
    }
}