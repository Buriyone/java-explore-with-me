package ru.practicum.subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.service.SubscriptionService;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с подписками.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/subscriptions")
public class SubscriptionController {
    /**
     * Предоставляет доступ к сервису подписок.
     */
    public final SubscriptionService subscriptionService;

    /**
     * Обрабатывает запросы на подписку.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribe(@RequestParam int userId,
                                     @RequestParam int subscriberId) {
        return subscriptionService.subscribe(userId, subscriberId);
    }

    /**
     * Обрабатывает запросы на предоставление списка подписок.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortDto> getSubscriptions(@RequestParam int subscriberId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return subscriptionService.getSubscriptions(subscriberId, from, size);
    }

    /**
     * Обрабатывает запросы на предоставление списка подписчиков.
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserShortDto> getSubscribers(@PathVariable int userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return subscriptionService.getSubscribers(userId, from, size);
    }

    /**
     * Обрабатывает запросы на отписку.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@RequestParam int userId,
                            @RequestParam int subscriberId) {
        subscriptionService.unsubscribe(userId, subscriberId);
    }

}
