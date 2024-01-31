package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.assistant.SortOption;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с событиями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@Valid @RequestBody NewEventDto newEventDto,
                            @PathVariable int userId) {
        return eventService.add(newEventDto, userId);
    }

    /**
     * Обрабатывает запросы на предоставление списка событий для организатора.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllByInitiator(@PathVariable int userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllByInitiator(userId, from, size);
    }

    /**
     * Обрабатывает запросы на предоставление событий для организатора по уникальному идентификатору.
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getByIdByInitiator(@PathVariable int userId,
                                           @PathVariable int eventId) {
        return eventService.getByIdByInitiator(userId, eventId);
    }

    /**
     * Обрабатывает запросы на обновление данных события организатором.
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByIdByInitiator(@PathVariable int userId,
                                              @PathVariable int eventId,
                                              @Valid @RequestBody UpdateEventUserRequest eventUserRequest) {
        return eventService.updateByIdByInitiator(userId, eventId, eventUserRequest);
    }

    /**
     * Обрабатывает запросы на предоставление ленты событий.
     */
    @GetMapping("/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getFeed(@PathVariable int userId,
                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                       @RequestParam(defaultValue = "EVENT_DATE") SortOption sort,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return eventService.getFeed(userId, onlyAvailable, sort, from, size);
    }
}
