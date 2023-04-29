package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.RealiseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    int id;
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Length(max = 200)
    String description;
    @RealiseDate(message = "дата релиза — не раньше 28 декабря 1895 года")
    LocalDate releaseDate;
    @Min(value = 0, message = "продолжительность фильма должна быть положительной.")
    Duration duration;
}
