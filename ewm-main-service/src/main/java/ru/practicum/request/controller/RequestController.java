package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с заявками.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class RequestController {
    /**
     * Предоставляет доступ к сервису заявок.
     */
    private final RequestService requestService;

    /**
     * Обрабатывает запросы регистрации и сохранения заявок.
     */
    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto add(@PathVariable int userId,
                                       @RequestParam int eventId) {
        return requestService.add(userId, eventId);
    }

    /**
     * Обрабатывает запросы предоставления списка заявок.
     */
    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> get(@PathVariable int userId) {
        return requestService.get(userId);
    }

    /**
     * Обрабатывает запросы на отмену заявок.
     */
    @PatchMapping("/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancel(@PathVariable int userId,
                                          @PathVariable int requestId) {
        return requestService.cancel(userId, requestId);
    }

    /**
     * Обрабатывает запросы на предоставление списка заявок для организатора.
     */
    @GetMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getByInitiator(@PathVariable int userId,
                                                        @PathVariable int eventId) {
        return requestService.getByInitiator(userId, eventId);
    }

    /**
     * Обрабатывает запросы на пре-модерацию статуса заявок на участие в событии.
     */
    @PatchMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult moderation(@PathVariable int userId,
                                                     @PathVariable int eventId,
                                                     @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        return requestService.moderation(userId, eventId, request);
    }
}