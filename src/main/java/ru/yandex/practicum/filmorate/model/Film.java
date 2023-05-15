package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validation.RealiseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    Integer duration;

    private final Set<Integer> likes = new HashSet<>();

    public void addLike(Integer id) {
        likes.add(id);
    }
}