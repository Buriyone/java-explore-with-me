package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.event.controller.EventController.PATTERN;

/**
 * Контроллер сервера для обработки запросов администратора связанных с событиями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    /**
     * Предоставляет доступ к сервису событий.
     */
    private final EventService eventService;

    /**
     * Обрабатывает запросы на предоставление списка событий для администратора.
     */
    @GetMapping
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
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByIdByAdmin(@PathVariable int eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest request) {
        return eventService.updateByIdByAdmin(eventId, request);
    }
}
