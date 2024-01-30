package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс пользователя в формате DTO упрощенный.
 */
@Data
@Builder(toBuilder = true)
public class UserShortDto {
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
}
