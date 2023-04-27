package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Field \"Email\" cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;
    @NotBlank(message = "Field \"login\" cannot be empty.")
    @Pattern(regexp = "\\S+", message = "Field \"login\" cannot contain whitespaces.")
    private String login;
    private String name;
    @PastOrPresent(message = "Value of field \"birthday\" cannot be in the future.")
    private LocalDate birthday;
}
