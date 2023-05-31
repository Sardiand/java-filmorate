package ru.yandex.practicum.filmorate.exception;

public class LikeIsExistException extends RuntimeException {

    public LikeIsExistException(String message) {
        super(message);
    }
}
