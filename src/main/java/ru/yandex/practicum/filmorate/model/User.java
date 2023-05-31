package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;

    @EqualsAndHashCode.Exclude
    @Email(message = "Invalid email format.")
    private String email;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Field \"login\" cannot be empty.")
    @Pattern(regexp = "\\S+", message = "Field \"login\" cannot contain whitespaces.")
    private String login;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    @PastOrPresent(message = "Value of field \"birthday\" cannot be in the future.")
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    private final Set<Long> friends = new HashSet<>();
}
