package ru.practicum.request.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.assistant.State;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.util.List;

/**
 * Репозиторий данных заявок.
 */
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    /**
     * Проверяет наличие заявки в базе.
     */
    boolean existsByEventAndRequester(Event event, User requester);

    /**
     * Осуществляет поиск и предоставление списка заявок пользователя.
     */
    List<ParticipationRequest> findAllByRequester(User requester, Sort sort);

    /**
     * Осуществляет поиск и предоставление списка заявок на участие в событии.
     */
    List<ParticipationRequest> findAllByEvent(Event event,Sort sort);

    /**
     * Осуществляет поиск и предоставление списка заявок на участие в событии по списку уникальных идентификаторов.
     */
    List<ParticipationRequest> findAllByIdIn(List<Integer> ids, Sort sort);

    /**
     * Предоставляет количество заявок на участие в событии с определенным статусом.
     */
    int countByEventAndStatus(Event event, State status);
}
