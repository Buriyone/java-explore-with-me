package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.assistant.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.model.ValidationException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса сервиса заявок.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    /**
     * Предоставляет доступ к репозиторию заявок.
     */
    private final RequestRepository requestRepository;
    /**
     * Предоставляет доступ к сервису пользователей.
     */
    private final UserService userService;
    /**
     * Предоставляет доступ к сервису событий.
     */
    private final EventService eventService;
    /**
     * Предоставляет доступ к репозиторию событий.
     */
    private final EventRepository eventRepository;
    /**
     * Предоставляет доступ к мапперу.
     */
    private final RequestMapper requestMapper;

    /**
     * Сервисный метод регистрации и сохранения заявки на участие в событии.
     * Генерирует {@link ConflictException} если событие еще не опубликовано или повторная отправка заявки
     * или заявку отправляет организатор или все билеты уже были реализованы.
     * @param userId уникальный идентификатор пользователя.
     * @param eventId уникальный идентификатор события.
     * @return возвращает зарегистрированную заявку в формате {@link ParticipationRequestDto}.
     */
    @Override
    @Transactional
    public ParticipationRequestDto add(int userId, int eventId) {
        log.info("Поступил запрос пользователя с id: {} " +
                "на регистрацию и сохранение заявки для участия в событии с id: {}.", userId, eventId);
        User requester = userService.findById(userId);
        Event event = eventService.findById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие еще не опубликовано.");
        }
        if (requestRepository.existsByEventAndRequester(event, requester)) {
            throw new ConflictException("Заявка уже была отправлена.");
        }
        if (requester.equals(event.getInitiator())) {
            throw new ConflictException("Организатор не может участвовать в событии.");
        }
        if (!event.getRequestModeration()
                && event.getParticipantLimit() != 0
                && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ConflictException("Sold out");
        }
        ParticipationRequest request = requestRepository.save(ParticipationRequest
                .builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(!event.getRequestModeration() || event.getParticipantLimit() == 0
                        ? State.CONFIRMED : State.PENDING)
                .build());
        eventConfirmedRequestsUpdater(event);
        return requestMapper.toParticipationRequestDto(request);
    }

    /**
     * Сервисный метод предоставления списка заявок пользователя.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает список заявок в формате {@link ParticipationRequestDto}.
     */
    @Override
    public List<ParticipationRequestDto> get(int userId) {
        log.info("Поступил запрос на предоставление списка заявок пользователя с id: {}.", userId);
        return requestRepository
                .findAllByRequester(userService.findById(userId), Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод отмены заявки на участие в событии.
     * @param userId уникальный идентификатор автора.
     * @param requestId уникальный идентификатор заявки.
     * @return возвращает отмененную заявку в формате {@link ParticipationRequestDto}.
     */
    @Override
    @Transactional
    public ParticipationRequestDto cancel(int userId, int requestId) {
        log.info("Поступил запрос пользователя с id: {} на отмену заявки участия в событии.", userId);
        ParticipationRequest request = findById(requestId);
        authorChecker(request, userId);
        request.setStatus(State.CANCELED);
        eventConfirmedRequestsUpdater(request.getEvent());
        return requestMapper.toParticipationRequestDto(request);
    }

    /**
     * Сервисный метод предоставления списка заявок для участия в событии.
     * Пользуется вспомогательным методом ownershipChecker() для проверки организатора.
     * @param userId уникальный идентификатор организатора.
     * @param eventId уникальный идентификатор события.
     * @return возвращает список заявок в формате {@link ParticipationRequestDto}.
     */
    @Override
    public List<ParticipationRequestDto> getByInitiator(int userId, int eventId) {
        log.info("Поступил запрос на предоставление списка заявок на участие для организатора c id: {}.", userId);
        Event event = eventService.findById(eventId);
        eventService.ownershipChecker(event, userId);
        return requestRepository.findAllByEvent(event, Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод пре-модерации статуса заявок на участие в событии.
     * @param userId уникальный идентификатор организатора события.
     * @param eventId уникальный идентификатор события.
     * @param request запрос со списком заявок и новым статусом в формате {@link EventRequestStatusUpdateRequest}.
     * @return возвращает результат с обновленным списком заявок в формате {@link EventRequestStatusUpdateResult};
     */
    @Override
    @Transactional
    public EventRequestStatusUpdateResult moderation(int userId, int eventId,
                                                     EventRequestStatusUpdateRequest request) {
        log.info("Поступил запрос на обновление статуса списка заявок для участия в событии с id: {}.", eventId);
        Event event = eventService.findById(eventId);
        eventService.ownershipChecker(event, userId);
        if (request.getStatus().equals(State.CONFIRMED)) {
            if (event.getParticipantLimit() == 0
                    || event.getParticipantLimit() >= (event.getConfirmedRequests() + request.getRequestIds().size())) {
                List<ParticipationRequestDto> requests = statusUpdater(request.getRequestIds(), State.CONFIRMED);
                eventConfirmedRequestsUpdater(event);
                return EventRequestStatusUpdateResult.builder()
                        .confirmedRequests(requests)
                        .build();
            } else {
                throw new ConflictException("Количество одобренных заявок превышает максимальное " +
                        "количество участников.");
            }
        } else if (request.getStatus().equals(State.REJECTED)) {
            List<ParticipationRequestDto> requests = statusUpdater(request.getRequestIds(), State.REJECTED);
            eventConfirmedRequestsUpdater(event);
            return EventRequestStatusUpdateResult.builder()
                    .rejectedRequests(requests)
                    .build();
        } else {
            throw new ConflictException("Статус указан некорректно.");
        }
    }

    /**
     * Вспомогательный метод обновления статуса и сохранения списка заявок.
     * @param ids список уникальных идентификаторов заявок.
     * @param status новый статус для заявок.
     * @return возвращает список сохраненных заявок с обновленным статусом в формате {@link ParticipationRequestDto}.
     */
    @Transactional
    private List<ParticipationRequestDto> statusUpdater(List<Integer> ids, State status) {
        log.debug("Осуществляется обновление и сохранение списка заявок на новый статус: {}.", status);
        for (int id : ids) {
            if (id == 0) {
                throw new ValidationException("Один или несколько уникальных идентификаторов не зарегистрированы.");
            }
        }
        return requestRepository.findAllByIdIn(ids, Sort.by(Sort.Direction.ASC, "id")).stream()
                .peek(participationRequest -> {
                    if (participationRequest.getStatus().equals(State.CONFIRMED)) {
                        throw new ConflictException(String.format("Заявка с id: %d уже была одобрена.",
                                participationRequest.getId()));
                    }
                    participationRequest.setStatus(status);
                })
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * Вспомогательный метод обновления списка подтвержденных заявок на участие в событии.
     * @param event событие в формате {@link Event}.
     */
    @Transactional
    private void eventConfirmedRequestsUpdater(Event event) {
        log.debug("Поступил запрос на обновление списка подтвержденных заявок на участие в событии.");
        event.setConfirmedRequests(requestRepository.countByEventAndStatus(event, State.CONFIRMED));
    }

    /**
     * Вспомогательный метод поиска заявки на участие в событии.
     * Генерирует {@link ValidationException} если заявка не зарегистрирована.
     * Генерирует {@link NotFoundException} если заявка не найдена.
     * @param requestId уникальный идентификатор заявки.
     * @return возвращает заявку в формате {@link ParticipationRequest}.
     */
    @Override
    public ParticipationRequest findById(int requestId) {
        log.debug("Осуществляется поиск заявки по уникальному идентификатору: {}.", requestId);
        if (requestId == 0) {
            throw new ValidationException("Уникальный идентификатор не зарегистрирован.");
        } else {
            return requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Заявка не найдена."));
        }
    }

    /**
     * Вспомогательный метод проверки пользователя на авторство заявки.
     * @param request объект заявки в формате {@link ParticipationRequest}.
     * @param userId уникальный идентификатор проверяемого автора.
     */
    private void authorChecker(ParticipationRequest request, int userId) {
        log.debug("Осуществляется проверка авторства заявки.");
        if (!request.getRequester().equals(userService.findById(userId))) {
            throw new ConflictException("Пользователь не является автором этой заявки.");
        }
    }
}