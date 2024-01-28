package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.assistant.SortOption;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с событиями.
 */
@RestController
@RequiredArgsConstructor
public class EventController {
    /**
     * Предоставляет доступ к сервису событий.
     */
    private final EventService eventService;
    /**
     * Паттерн формата времени.
     */
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Обрабатывает запросы на регистрацию и сохранение события.
     */
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@Valid @RequestBody NewEventDto newEventDto,
                            @PathVariable int userId) {
        return eventService.add(newEventDto, userId);
    }

    /**
     * Обрабатывает запросы на предоставление списка событий для организатора.
     */
    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllByInitiator(@PathVariable int userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllByInitiator(userId, from, size);
    }

    /**
     * Обрабатывает запросы на предоставление событий для организатора по уникальному идентификатору.
     */
    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getByIdByInitiator(@PathVariable int userId,
                                           @PathVariable int eventId) {
        return eventService.getByIdByInitiator(userId, eventId);
    }

    /**
     * Обрабатывает запросы на обновление данных события организатором.
     */
    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByIdByInitiator(@PathVariable int userId,
                                              @PathVariable int eventId,
                                              @Valid @RequestBody UpdateEventUserRequest eventUserRequest) {
        return eventService.updateByIdByInitiator(userId, eventId, eventUserRequest);
    }

    /**
     * Обрабатывает запрос на предоставление события по уникальному идентификатору.
     */
    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable int eventId, HttpServletRequest request) {
        return eventService.getById(eventId, request);
    }

    /**
     * Обрабатывает запросы на предоставление списка событий.
     */
    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) Integer[] categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN)
                                             LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN)
                                             LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") SortOption sortOption,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sortOption, from, size, request);
    }

    /**
     * Обрабатывает запросы на предоставление списка событий для администратора.
     */
    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) Integer[] users,
                                               @RequestParam(required = false) String[] states,
                                               @RequestParam(required = false) Integer[] categories,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN)
                                                   LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN)
                                                   LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Обрабатывает запросы на обновление события администратором.
     */
    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByIdByAdmin(@PathVariable int eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest request) {
        return eventService.updateByIdByAdmin(eventId, request);
    }
}
