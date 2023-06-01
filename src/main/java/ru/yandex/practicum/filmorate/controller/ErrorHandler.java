package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ExceptionValidate;
import ru.yandex.practicum.filmorate.exceptions.ExceptionsUpdate;

import java.util.Map;

@RestControllerAdvice
@Getter
public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerBadRequestException(final ExceptionValidate o) {
        log.error(o.getMessage());
        return Map.of("error", "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final ExceptionsUpdate o) {
        log.error(o.getMessage());
        return Map.of("error", "Объект не найден");
    }
}