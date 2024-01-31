package ru.practicum.subscription.service;

import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

/**
 * Интерфейс сервиса подписок.
 */
public interface SubscriptionService {
    SubscriptionDto subscribe(int userId, int subscriberId);

    List<UserShortDto> getSubscriptions(int subscriberId, int from, int size);

    List<UserShortDto> getSubscribers(int userId, int from, int size);

    void unsubscribe(int userId, int subscriberId);
}
