package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
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
}
