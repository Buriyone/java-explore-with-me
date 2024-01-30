package ru.practicum.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.assistant.State;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс заявки на участие в событии.
 */
@Data
@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipationRequest {
    /**
     * Уникальный идентификатор заявки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Дата и время создания заявки.
     */
    private LocalDateTime created;
    /**
     * Событие.
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    /**
     * Уникальный идентификатор пользователя, отправившего заявку.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;
    /**
     * Статус заявки.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State status;
}
