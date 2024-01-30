package ru.practicum.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Сущность категории.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
@Builder(toBuilder = true)
public class Category {
    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Название категории.
     */
    @Column(name = "category_name")
    @NotBlank(message = "Название категории не может быть пустым или отсутствовать")
    @Size(max = 50, message = "Длина названия не должна превышать 50 символов.")
    private String name;
}
