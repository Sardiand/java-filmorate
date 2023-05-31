package ru.yandex.practicum.filmorate.exception;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class UpdateValidationException extends RuntimeException {

    public UpdateValidationException(String message) {
        super(message);
    }
}
