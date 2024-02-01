package ru.practicum.subscription.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс подписки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
@Builder(toBuilder = true)
public class Subscription {
    /**
     * Уникальный идентификатор подписки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Пользователь на которого подписываются.
     */
    @NotNull(message = "Пользователь на которого подписываются не может отсутствовать.")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * Пользователь - подписчик.
     */
    @NotNull(message = "Подписчик не может отсутствовать.")
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;
    /**
     * Дата и время подписки.
     */
    @NotNull(message = "Дата и время подписки не может отсутствовать.")
    @Column(name = "created_on")
    private LocalDateTime created;
}
