package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс категории в формате DTO.
 */
@Data
@Builder(toBuilder = true)
public class CategoryDto {
    /**
     * Уникальный идентификатор категории.
     */
    private int id;
    /**
     * Название категории.
     */
    @NotBlank(message = "Название категории не может быть пустым или отсутствовать")
    @Size(max = 50, message = "Длина названия не должна превышать 50 символов.")
    private String name;
}
