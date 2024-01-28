package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.List;

/**
 * Репозиторий данных событий.
 */
public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {
    /**
     * Предоставляет список событий организатора.
     */
    Page<Event> findAllByInitiator(User user, Pageable pageable);

    /**
     * Предоставляет список событий по списку уникальных идентификаторов.
     */
    List<Event> findAllByIdIn(List<Integer>ids);

    /**
     * Проверяет наличие события с категорией.
     */
    boolean existsByCategory(Category category);
}
