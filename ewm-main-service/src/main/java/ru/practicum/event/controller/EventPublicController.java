package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.assistant.SortOption;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.event.controller.EventController.PATTERN;

/**
 * Контроллер сервера для обработки публичных запросов связанных с событиями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {
    /**
     * Предоставляет доступ к сервису событий.
     */
    private final EventService eventService;

    /**
     * Обрабатывает запрос на предоставление события по уникальному идентификатору.
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable int eventId, HttpServletRequest request) {
        return eventService.getById(eventId, request);
    }

    /**
     * Обрабатывает запросы на предоставление списка событий.
     */
    @GetMapping
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
}
