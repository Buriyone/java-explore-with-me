package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;
import ru.practicum.assistant.SortOption;
import ru.practicum.assistant.State;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.model.ValidationException;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.service.ClientService;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.event.controller.EventController.PATTERN;


/**
 * Реализация интерфейса сервиса событий.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    /**
     * Предоставляет доступ к клиенту сервиса статистики.
     */
    public final ClientService statService;
    /**
     * Форматтер времени.
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
    /**
     * Предоставляет доступ к мапперу событий.
     */
    private final EventMapper eventMapper;
    /**
     * Предоставляет доступ к сервису категорий.
     */
    private final CategoryService categoryService;
    /**
     * Предоставляет доступ к сервису пользователей.
     */
    private final UserService userService;
    /**
     * Предоставляет доступ к репозиторию координат.
     */
    private final LocationRepository locationRepository;
    /**
     * Предоставляет доступ к репозиторию событий.
     */
    private final EventRepository eventRepository;

    /**
     * Сервисный метод регистрации и сохранения события.
     * Использует вспомогательный метод проверки валидации даты и времени dateValidation.
     * @param newEventDto объект нового события подлежащий регистрации и сохранению.
     * @param userId уникальный идентификатор пользователя-организатора.
     * @return возвращает зарегистрированное событие в формате {@link EventFullDto}.
     */
    @Override
    @Transactional
    public EventFullDto add(NewEventDto newEventDto, int userId) {
        log.info("Поступил запрос на регистрацию и сохранение события пользователем с id: {}", userId);
        Event event = eventMapper.toEvent(newEventDto);
        dateValidation(event.getEventDate());
        event.setCategory(categoryService.findById(newEventDto.getCategory()));
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userService.findById(userId));
        event.setLocation(locationRepository.save(newEventDto.getLocation()));
        event.setState(State.PENDING);
        event.setViews(0);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    /**
     * Сервисный метод предоставления списка событий по параметрам для организатора.
     * @param userId уникальный идентификатор организатора событий.
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора.
     * @param size количество элементов в наборе.
     * @return возвращает список событий в формате {@link EventFullDto}.
     */
    @Override
    public List<EventFullDto> getAllByInitiator(int userId, int from, int size) {
        log.info("Поступил запрос на предоставление списка событий для организатора с id: {}.", userId);
        return eventRepository.findAllByInitiator(userService.findById(userId),
                PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод предоставления события для организатора по уникальному идентификатору.
     * Генерирует {@link ValidationException} если данные указаны некорректно или отсутствуют права.
     * @param userId уникальный идентификатор организатора.
     * @param eventId уникальный идентификатор события.
     * @return возвращает событие в формате {@link EventFullDto}.
     */
    @Override
    public EventFullDto getByIdByInitiator(int userId, int eventId) {
        log.info("Поступил запрос на предоставление события " +
                "по уникальному идентификатору для организатора с id: {}.", userId);
        Event event = findById(eventId);
        ownershipChecker(event, userId);
        log.info("Событие предоставлено организатору.");
        return eventMapper.toEventFullDto(event);
    }

    /**
     * Сервисный метод редактирования события организатором.
     * Генерирует {@link ConflictException} если редактируемое событие было опубликовано или нет прав администратора.
     * Использует вспомогательный метод проверки пользователя на причастность к организации события.
     * Использует вспомогательный метод присвоения данных подлежащих обновлению.
     * @param userId уникальный идентификатор организатора.
     * @param eventId уникальный идентификатор события.
     * @param eventUserRequest объект события в формате {@link UpdateEventUserRequest} с данными для редактирования.
     * @return возвращает отредактированное событие в формате {@link EventFullDto}.
     */
    @Override
    @Transactional
    public EventFullDto updateByIdByInitiator(int userId, int eventId, UpdateEventUserRequest eventUserRequest) {
        log.info("Поступил запрос на обновление события по уникальному идентификатору организатором с id: {}.", userId);
        Event toUpdatedEvent = findById(eventId);
        ownershipChecker(toUpdatedEvent, userId);
        if (toUpdatedEvent.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Нельзя редактировать опубликованное событие.");
        }
        Event updatedEvent = eventUpdater(toUpdatedEvent, eventUserRequest);
        if (eventUserRequest.getStateAction() != null) {
            switch (State.valueOf(eventUserRequest.getStateAction())) {
                case CANCEL_REVIEW:
                    updatedEvent.setPublishedOn(LocalDateTime.now());
                    updatedEvent.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    updatedEvent.setPublishedOn(LocalDateTime.now());
                    updatedEvent.setState(State.PENDING);
                    break;
                default:
                    throw new ConflictException("Нет прав администратора.");
            }
        }
        return eventMapper.toEventFullDto(eventRepository.save(updatedEvent));
    }

    /**
     * Сервисный метод предоставления события по уникальному идентификатору.
     * Пользуется вспомогательным методом сохранения статистики saveStat().
     * Генерирует {@link NotFoundException} если событие еще не опубликовано.
     * @param eventId уникальный идентификатор события.
     * @param request запрос с данными пользователя.
     * @return возвращает событие в формате {@link EventFullDto}.
     */
    @Override
    @Transactional
    public EventFullDto getById(int eventId, HttpServletRequest request) {
        log.info("Поступил запрос на предоставление события по уникальному идентификатору: {}", eventId);
        Event event = findById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Событие с id: %d еще не опубликовано.",
                    eventId));
        }
        saveStat(request);
        event.setViews(getViews(event));
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    /**
     * Сервисный метод предоставления событий по параметрам.
     * @param text текст для поиска в содержимом аннотации и подробном описании события.
     * @param categories список идентификаторов категорий в которых будет вестись поиск.
     * @param paid поиск только платных/бесплатных событий.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd дата и время не позже которых должно произойти событие.
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие.
     * @param sort Вариант сортировки: по дате события или по количеству просмотров
     * @param from количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size количество событий в наборе.
     * @return возвращает список событий в формате {@link EventShortDto}.
     */
    @Override
    public List<EventShortDto> getEvents(String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, SortOption sort,
                                         int from, int size, HttpServletRequest request) {
        log.info("Поступил запрос на предоставление событий по параметрам.");
        BooleanBuilder builder = new BooleanBuilder();
        if (text != null && !text.isBlank()) {
            BooleanExpression byAnnotation = QEvent.event.annotation.containsIgnoreCase(text);
            BooleanExpression byDescription = QEvent.event.description.containsIgnoreCase(text);
            builder.and(byAnnotation).or(byDescription);
        }
        if (categories != null) {
            BooleanExpression byCategoryId = QEvent.event.category.id.in(categories);
            builder.and(byCategoryId);
        }
        if (paid != null) {
            BooleanExpression byPaid = QEvent.event.paid.eq(paid);
            builder.and(byPaid);
        }
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidationException("Конечное время не может быть раньше начального.");
            } else {
                BooleanExpression byDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
                builder.and(byDate);
            }
        }
        if (rangeStart != null && rangeEnd == null) {
            BooleanExpression byDate = QEvent.event.eventDate.after(rangeStart);
            builder.and(byDate);
        }
        if (rangeStart == null && rangeEnd != null) {
            BooleanExpression byDate = QEvent.event.eventDate.before(rangeEnd);
            builder.and(byDate);
        }
        if (onlyAvailable.equals(true)) {
            BooleanExpression byLimitFree = QEvent.event.participantLimit.eq(0);
            BooleanExpression byLimitConfirm = QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit);
            builder.and(byLimitFree).or(byLimitConfirm);
        }
        List<EventShortDto> events = eventRepository.findAll(builder,
                        PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate")))
                .getContent()
                .stream()
                .peek(event -> {
                    saveStat(request);
                    event.setViews(getViews(event));
                })
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        if (sort.equals(SortOption.VIEWS)) {
            return events.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else {
            return events;
        }
    }

    /**
     * Сервисный метод предоставления событий администратору по параметрам.
     * @param users список уникальных идентификаторов пользователей.
     * @param states список статусов состояния события.
     * @param categories список уникальных идентификаторов категорий.
     * @param rangeStart дата и время не раньше которых должно произойти событие.
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param from количество событий, которые нужно пропустить для формирования текущего набора.
     * @param size количество событий в наборе.
     * @return возвращает список событий в формате {@link EventFullDto}.
     */
    @Override
    public List<EventFullDto> getEventsByAdmin(Integer[] users, String[] states, Integer[] categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.info("Поступил запрос на предоставление событий администратору по параметрам.");
        BooleanBuilder builder = new BooleanBuilder();
        if (users != null) {
            BooleanExpression byUserId = QEvent.event.initiator.id.in(users);
            builder.and(byUserId);
        }
        if (states != null) {
            BooleanExpression byStateId = QEvent.event.state.in(Arrays.stream(states)
                    .map(State::valueOf)
                    .collect(Collectors.toList()));
            builder.and(byStateId);
        }
        if (categories != null) {
            BooleanExpression byCategoryId = QEvent.event.category.id.in(categories);
            builder.and(byCategoryId);
        }
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ConflictException("Конечное время не может быть раньше начального.");
            } else {
                BooleanExpression byDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
                builder.and(byDate);
            }
        }
        if (rangeStart != null && rangeEnd == null) {
            BooleanExpression byDate = QEvent.event.eventDate.after(rangeStart);
            builder.and(byDate);
        }
        if (rangeStart == null && rangeEnd != null) {
            BooleanExpression byDate = QEvent.event.eventDate.before(rangeEnd);
            builder.and(byDate);
        }
        return eventRepository.findAll(builder, PageRequest.of(from, size,
                        Sort.by(Sort.Direction.ASC, "id")))
                .getContent()
                .stream()
                .peek(event -> event.setViews(getViews(event)))
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод обновления данных события администратором.
     * @param eventId уникальный идентификатор события.
     * @param request объект события в формате {@link UpdateEventAdminRequest} с данными для редактирования.
     * @return возвращает отредактированное событие в формате {@link EventFullDto}.
     */
    @Override
    @Transactional
    public EventFullDto updateByIdByAdmin(int eventId, UpdateEventAdminRequest request) {
        log.info("Поступил запрос на обновление данных события с id: {} администратором.", eventId);
        Event event = findById(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ConflictException("Статус события %d не позволяет изменить данные.");
        }
        if (LocalDateTime.now().isAfter(event.getEventDate().minusHours(1))) {
            throw new ValidationException("Дата начала изменяемого события должна быть " +
                    "не ранее чем за час от даты публикации.");
        }
        if (request.getEventDate() != null
                && LocalDateTime.now().isAfter(LocalDateTime.parse(request.getEventDate(), formatter))) {
            throw new ValidationException("Дата начала изменяемого события не может быть в прошлом.");
        }
        Event upatedEvent = eventUpdater(event, eventMapper.toUpdateEventUserRequest(request));
        if (request.getStateAction() == null || request.getStateAction().equals(State.PUBLISH_EVENT.toString())) {
            upatedEvent.setState(State.PUBLISHED);
        } else if (request.getStateAction().equals(State.REJECT_EVENT.toString())) {
            upatedEvent.setState(State.CANCELED);
        } else {
            throw new ConflictException("Статус для обновления указан некорректно.");
        }
        upatedEvent.setPublishedOn(LocalDateTime.now());
        return eventMapper.toEventFullDto(eventRepository.save(upatedEvent));
    }

    /**
     * Вспомогательный метод поиска события по уникальному идентификатору.
     * Генерирует {@link ValidationException} если событие не было зарегистрировано.
     * Генерирует {@link NotFoundException} если событие не найдено.
     * @param eventId уникальный идентификатор события.
     * @return возвращает событие в формате {@link Event}.
     */
    @Override
    public Event findById(int eventId) {
        log.debug("Осуществляется поиск события по уникальному идентификатору: {}.", eventId);
        if (eventId == 0) {
            throw new ValidationException("Уникальный идентификатор не зарегистрирован.");
        } else {
            return eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Событие не найдено."));
        }
    }

    /**
     * Вспомогательный метод поиска и предоставление списка событий по списку уникальных идентификаторов.
     * @param ids список уникальных идентификаторов.
     * @return возвращает список уникальных идентификаторов в формате {@link Event}.
     */
    @Override
    public List<Event> findAllByIds(List<Integer> ids) {
        log.debug("Осуществляется поиск и предоставление списка событий по списку уникальных идентификаторов.");
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        for (Integer id : ids) {
            if (id == 0) {
                throw new ValidationException("Уникальный идентификатор не зарегистрирован.");
            }
        }
        return eventRepository.findAllByIdIn(ids);
    }

    /**
     * Вспомогательный метод проверки пользователя на причастность к организации события.
     * @param event объект события в формате {@link Event}.
     * @param userId уникальный идентификатор проверяемого организатора.
     */
    @Override
    public void ownershipChecker(Event event, int userId) {
        log.debug("Осуществляется проверка организатора события.");
        if (!event.getInitiator().equals(userService.findById(userId))) {
            throw new ConflictException("Пользователь не является организатором этого события.");
        }
    }

    /**
     * Вспомогательный метод присвоения данных подлежащих обновлению.
     * @param updatedEvent объект события подлежащий обновлению в формате {@link Event}.
     * @param sampleEvent объект события несущий данные для обновления в формате {@link UpdateEventUserRequest}.
     * @return возвращает объект события с обновленными данными.
     */
    private Event eventUpdater(Event updatedEvent, UpdateEventUserRequest sampleEvent) {
        log.debug("Осуществляется присвоение параметров события.");
        if (sampleEvent.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(sampleEvent.getEventDate(), formatter);
            dateValidation(newEventDate);
            updatedEvent.setEventDate(newEventDate);
        }
        if (sampleEvent.getAnnotation() != null) {
            updatedEvent.setAnnotation(sampleEvent.getAnnotation());
        }
        if (sampleEvent.getCategory() != 0) {
            updatedEvent.setCategory(categoryService.findById(sampleEvent.getCategory()));
        }
        if (sampleEvent.getDescription() != null) {
            updatedEvent.setDescription(sampleEvent.getDescription());
        }
        if (sampleEvent.getLocation() != null) {
            updatedEvent.setLocation(locationRepository.save(sampleEvent.getLocation()));
        }
        if (sampleEvent.getPaid() != null) {
            updatedEvent.setPaid(sampleEvent.getPaid());
        }
        if (sampleEvent.getParticipantLimit() != 0) {
            updatedEvent.setParticipantLimit(sampleEvent.getParticipantLimit());
        }
        if (sampleEvent.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(sampleEvent.getRequestModeration());
        }
        if (sampleEvent.getTitle() != null) {
            updatedEvent.setTitle(sampleEvent.getTitle());
        }
        return updatedEvent;
    }

    /**
     * Вспомогательный метод валидации времени.
     * Генерирует {@link ConflictException} если время указано некорректно.
     * @param date объект времени.
     */
    private void dateValidation(LocalDateTime date) {
        log.debug("Осуществляется проверка валидности даты.");
        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего времени.");
        }
    }

    /**
     * Вспомогательный метод сохранения статистики запросов.
     * @param request запрос с данными пользователя.
     */
    private void saveStat(HttpServletRequest request) {
        log.info("Производится сохранение статистики.");
        statService.add(StatDto.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
    }

    /**
     * Вспомогательный метод предоставления количества просмотров.
     * @param event событие в формате {@link Event}
     * @return возвращает количество просмотров данного события.
     */
    private int getViews(Event event) {
        log.info("Производится предоставление количества просмотров события.");
            List<StatResponseDto> response = statService.get(LocalDateTime.now().minusYears(1),
                    LocalDateTime.now().plusDays(1), new String[]{"/events/" + event.getId()}, true);
            if (!response.isEmpty()) {
                String[] strings = String.valueOf(response.get(0)).split("=");
                return Integer.parseInt(strings[strings.length - 1].split("}")[0]);
            } else {
                return 0;
            }
    }
}