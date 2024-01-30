package ru.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder(toBuilder = true)
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Имя пользователя не может быть пустым или отсутствовать.")
    @Size(min = 2, max = 250, message = "Имя пользователя не может быть меньше 2 знаков и больше 250.")
    @Column(name = "user_name")
    private String name;
    /**
     * Уникальный идентификатор пользователя.
     */
    @NotBlank(message = "Электронная почта не может быть пустой или отсутствовать.")
    @Size(min = 6, max = 254, message = "Электронная почта не может быть меньше 2 знаков и больше 250.")
    @Email(message = "Электронная почта указана некорректно.")
    @Column(name = "user_email")
    private String email;
}