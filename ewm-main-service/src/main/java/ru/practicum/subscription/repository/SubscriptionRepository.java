package ru.practicum.subscription.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.user.model.User;

/**
 * Репозиторий данных подписок.
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    boolean existsByUserAndSubscriber(User user, User subscriber);

    Page<Subscription> findAllBySubscriber(User subscriber, Pageable pageable);

    Page<Subscription> findAllByUser(User user, Pageable pageable);

    Subscription findByUserAndSubscriber(User user, User subscriber);
}
