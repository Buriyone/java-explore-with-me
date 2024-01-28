package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;
import javax.validation.constraints.Size;

/**
 * Класс обновляемого события в формате DTO для администратора.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateEventAdminRequest {
    /**
     * Уникальный идентификатор события.
     */
    private int id;
    /**
     * Краткое описание события.
     */
    @Size(min = 20, max = 2000, message = "Размер аннотации должен быть не менее 20 символов и не более 2000.")
    private String annotation;
    /**
     * Уникальный идентификатор категории к которой относится событие.
     */
    private int category;
    /**
     * Полное описание события.
     */
    @Size(min = 20, max = 7000, message = "Размер описания должен быть не менее 20 символов и не более 7000.")
    private String description;
    /**
     * Дата и время на которые намечено событие.
     */
    private String eventDate;
    /**
     * Широта и долгота места проведения события.
     */
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
     * Изменение состояния события.
     */
    private String stateAction;
    /**
     * Заголовок события.
     */
    @Size(min = 3, max = 120, message = "Заголовок события должен быть не менее 3 символов и не более 120.")
    private String title;
}
