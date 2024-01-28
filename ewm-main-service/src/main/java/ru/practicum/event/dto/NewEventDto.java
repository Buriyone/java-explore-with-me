package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс нового события в формате DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewEventDto {
    /**
     * Уникальный идентификатор события.
     */
    private int id;
    /**
     * Краткое описание события.
     */
    @NotBlank(message = "Аннотация не может отсутствовать или быть пустой.")
    @Size(min = 20, max = 2000, message = "Размер аннотации должен быть не менее 20 символов и не более 2000.")
    private String annotation;
    /**
     * Уникальный идентификатор категории к которой относится событие.
     */
    @NotNull(message = "Категория не может отсутствовать.")
    private int category;
    /**
     * Полное описание события.
     */
    @NotBlank(message = "Описание не может отсутствовать или быть пустым.")
    @Size(min = 20, max = 7000, message = "Размер описания должен быть не менее 20 символов и не более 7000.")
    private String description;
    /**
     * Дата и время на которые намечено событие.
     */
    @NotBlank(message = "Дата события не может отсутствовать или быть пустой.")
    private String eventDate;
    /**
     * Широта и долгота места проведения события.
     */
    @NotNull(message = "Координаты не могут отсутствовать.")
    private Location location;
    /**
     * Необходимость оплачивать участие в событии.
     */
    private Boolean paid;
    /**
     * Ограничение на количество участников.
     * Значение 0 - означает отсутствие ограничения.
     */
    private int participantLimit;
    /**
     * Необходимость пре-модерации заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения инициатором события.
     * Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;
    /**
     * Заголовок события.
     */
    @NotBlank(message = "Заголовок события не может отсутствовать или быть пустым.")
    @Size(min = 3, max = 120, message = "Заголовок события должен быть не менее 3 символов и не более 120.")
    private String title;
}