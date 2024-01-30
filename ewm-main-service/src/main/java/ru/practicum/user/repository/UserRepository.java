package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

/**
 * Репозиторий данных пользователей.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Проверяет наличие имени в базе.
     */
    boolean existsByName(String name);

    /**
     * Проверяет наличие электронной почты в базе.
     */
    boolean existsByEmail(String email);

    /**
     * Постранично находит пользователей в базе по массиву с уникальными идентификаторами.
     */
    Page<User> findAllByIdIn(int[] ids, Pageable pageable);

    /**
     * Проверяет наличие пользователей по списку.
     */
    boolean existsByIdIn(int[] userIds);
}
