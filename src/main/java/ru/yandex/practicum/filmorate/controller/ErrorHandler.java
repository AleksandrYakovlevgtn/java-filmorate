package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> handlerBadRequestException(final ExceptionValidate o) {
        String error = "400 BAD_REQUEST.";
        String message = o.getMessage();
        log.error(error + " " + message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error + " " + message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final ExceptionsUpdate o) {
        /*String error = "404 NOT_FOUND.";
        String message = o.getMessage();
        log.error(error + " " + message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error + " " + message);*/
        return Map.of("error","Обект на найден");
    }
}