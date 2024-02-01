package ru.practicum.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserShortDto;
import javax.validation.constraints.NotNull;

/**
 * Класс подписки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SubscriptionDto {
    /**
     * Уникальный идентификатор подписки.
     */
    @NotNull(message = "Уникальный идентификатор подписки не может отсутствовать.")
    private int id;
    /**
     * Пользователь на которого подписываются.
     */
    @NotNull(message = "Пользователь на которого подписываются не может отсутствовать.")
    private UserShortDto user;
    /**
     * Пользователь - подписчик.
     */
    @NotNull(message = "Подписчик не может отсутствовать.")
    private UserShortDto subscriber;
}
