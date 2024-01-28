package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.assistant.State;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс полной информации о событии в формате DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventFullDto {
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
     * Категория события.
     */
    @NotNull(message = "Категория события не может отсутствовать.")
    private CategoryDto category;
    /**
     * Количество одобренных заявок на участие в событии.
     */
    private int confirmedRequests;
    /**
     * Дата и время создания события.
     */
    private String createdOn;
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
     * Краткая информация о пользователе-инициаторе события.
     */
    @NotNull(message = "Информация о пользователе не может отсутствовать.")
    private UserShortDto initiator;
    /**
     * Широта и долгота места проведения события.
     */
    @NotNull(message = "Координаты не могут отсутствовать.")
    private Location location;
    /**
     * Необходимость оплачивать участие в событии.
     */
    @NotNull(message = "Необходимость оплаты не может отсутствовать.")
    private Boolean paid;
    /**
     * Ограничение на количество участников.
     * Значение 0 - означает отсутствие ограничения.
     */
    private int participantLimit;
    /**
     * Дата и время публикации события.
     */
    private String publishedOn;
    /**
     * Необходимость пре-модерации заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения инициатором события.
     * Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;
    /**
     * Список состояний жизненного цикла события.
     */
    private State state;
    /**
     * Заголовок события.
     */
    @NotBlank(message = "Заголовок события не может отсутствовать или быть пустым.")
    @Size(min = 3, max = 120, message = "Заголовок события должен быть не менее 3 символов и не более 120.")
    private String title;
    /**
     * Количество просмотрев события.
     */
    private int views;
}