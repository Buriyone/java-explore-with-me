package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.assistant.State;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Класс события.
 */
@Data
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Event {
    /**
     * Уникальный идентификатор события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @NotNull(message = "Категория не может отсутствовать.")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    /**
     * Количество одобренных заявок на участие в событии.
     */
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    /**
     * Дата и время создания события.
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    /**
     * Полное описание события.
     */
    @NotBlank(message = "Описание не может отсутствовать или быть пустым.")
    @Size(min = 20, max = 7000, message = "Размер описания должен быть не менее 20 символов и не более 7000.")
    private String description;
    /**
     * Дата и время на которые намечено событие.
     */
    @NotNull(message = "Дата события не может отсутствовать или быть пустой.")
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    /**
     * Краткая информация о пользователе-инициаторе события.
     */
    @NotNull(message = "Информация о пользователе не может отсутствовать.")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;
    /**
     * Широта и долгота места проведения события.
     */
    @NotNull(message = "Координаты не могут отсутствовать.")
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    /**
     * Необходимость оплачивать участие в событии.
     */
    @NotNull(message = "Необходимость оплаты должна быть указана.")
    private Boolean paid;
    /**
     * Ограничение на количество участников.
     * Значение 0 - означает отсутствие ограничения.
     */
    @Column(name = "participant_limit")
    private int participantLimit;
    /**
     * Дата и время публикации события.
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    /**
     * Необходимость пре-модерации заявок на участие.
     * Если true, то все заявки будут ожидать подтверждения организатором события.
     * Если false - то будут подтверждаться автоматически.
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    /**
     * Список состояний жизненного цикла события.
     */
    @Enumerated(EnumType.STRING)
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