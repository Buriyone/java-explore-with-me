package ru.practicum.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.mapper.SubscriptionMapper;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.subscription.repository.SubscriptionRepository;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса сервиса подписок.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    /**
     * Предоставляет доступ к репозиторию подписок.
     */
    private final SubscriptionRepository subscriptionRepository;
    /**
     * Предоставляет доступ к сервису пользователей.
     */
    private final UserService userService;
    /**
     * Предоставляет доступ к мапперу подписок.
     */
    private final SubscriptionMapper subscriptionMapper;

    /**
     * Сервисный метод оформления подписки.
     * @param userId уникальный идентификатор пользователя на которого оформляется подписка.
     * @param subscriberId уникальный идентификатор пользователя который оформляет подписку.
     * @return возвращает зарегистрированную и оформленную подписку в формате {@link SubscriptionDto}.
     */
    @Override
    @Transactional
    public SubscriptionDto subscribe(int userId, int subscriberId) {
        log.info("Поступил запрос на подписку от пользователя c id: {} на пользователя с id: {}", subscriberId, userId);
        User user = userService.findById(userId);
        User subscriber = userService.findById(subscriberId);
        if (user.equals(subscriber)) {
            throw new ConflictException("Вы не можете подписаться сами на себя.");
        }
        if (subscriptionRepository.existsByUserAndSubscriber(user, subscriber)) {
            throw new ConflictException("Вы уже являетесь подписчиком.");
        }
        return subscriptionMapper.toSubscriptionDto(subscriptionRepository.save(Subscription.builder()
                .user(user)
                .subscriber(subscriber)
                .created(LocalDateTime.now())
                .build()));
    }

    /**
     * Сервисный метод предоставления списка подписок (пользователей на которых оформлена подписка).
     * @param subscriberId уникальный идентификатор пользователя-подписчика.
     * @param from количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size количество событий в наборе.
     * @return возвращает список подписок в формате {@link UserShortDto}.
     */
    @Override
    public List<UserShortDto> getSubscriptions(int subscriberId, int from, int size) {
        log.info("Поступил запрос от пользователя с id: {} на предоставления списка подписок.", subscriberId);
        User subscriber = userService.findById(subscriberId);
        return subscriptionRepository.findAllBySubscriber(subscriber, PageRequest.of(from, size,
                        Sort.Direction.DESC, "created")).stream()
                .map(subscriptionMapper::toSubscriptionDto)
                .map(SubscriptionDto::getUser)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод предоставления списка подписчиков (пользователей которые подписаны на вас).
     * @param userId уникальный идентификатор пользователя.
     * @param from количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size количество событий в наборе.
     * @return возвращает список подписчиков в формате {@link UserShortDto}.
     */
    @Override
    public List<UserShortDto> getSubscribers(int userId, int from, int size) {
        log.info("Поступил запрос от пользователя с id: {} на предоставления списка подписчиков.", userId);
        User user = userService.findById(userId);
        return subscriptionRepository.findAllByUser(user, PageRequest.of(from, size,
                        Sort.Direction.DESC, "created")).stream()
                .map(subscriptionMapper::toSubscriptionDto)
                .map(SubscriptionDto::getUser)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод отписка.
     * @param userId уникальный идентификатор пользователя от которого подписывается подписчик.
     * @param subscriberId уникальный идентификатор пользователя-подписчика.
     */
    @Override
    @Transactional
    public void unsubscribe(int userId, int subscriberId) {
        log.info("Поступил запрос на отписку от пользователя c id: {} пользователем с id: {}", userId, subscriberId);
        User user = userService.findById(userId);
        User subscriber = userService.findById(subscriberId);
        if (user.equals(subscriber)) {
            throw new ConflictException("Вы не можете отписаться сами от себя.");
        }
        if (!subscriptionRepository.existsByUserAndSubscriber(user, subscriber)) {
            throw new ConflictException("Вы не являетесь подписчиком.");
        }
        subscriptionRepository.delete(subscriptionRepository.findByUserAndSubscriber(user, subscriber));
    }
}
