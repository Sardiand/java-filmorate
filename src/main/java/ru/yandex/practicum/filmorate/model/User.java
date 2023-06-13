package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    private Long id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Invalid email format.")
    @NotNull(message = "Invalid email format.")
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

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @EqualsAndHashCode.Exclude
    private Set<Long> friends = new HashSet<>();

    public void setNameIfBlank() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}
