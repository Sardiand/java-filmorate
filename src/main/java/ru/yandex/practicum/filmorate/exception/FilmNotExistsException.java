package ru.yandex.practicum.filmorate.exception;

public class FilmNotExistsException extends RuntimeException {

    public FilmNotExistsException (String message) {
        super(message);
    }
}
