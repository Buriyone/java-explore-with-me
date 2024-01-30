package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс пользователя в формате DTO.
 */
@Data
@Builder(toBuilder = true)
public class UserDto {
    /**
     * Уникальный идентификатор пользователя.
     */
    private int id;
    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Имя пользователя не может быть пустым или отсутствовать.")
    @Size(min = 2, max = 250, message = "Имя пользователя не может быть меньше 2 знаков и больше 250.")
    private String name;
    /**
     * Уникальный идентификатор пользователя.
     */
    @NotBlank(message = "Электронная почта не может быть пустой или отсутствовать.")
    @Size(min = 6, max = 254, message = "Электронная почта не может быть меньше 2 знаков и больше 250.")
    @Email(message = "Электронная почта указана некорректно.")
    private String email;
}
