package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер сервера для обработки статистики.
 */
@RestController
@RequiredArgsConstructor
public class StatController {
    /**
     * Константа формата даты и времени.
     */
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * Предоставляет доступ к сервису статистики.
     */
    private final StatService service;

    /**
     * Обрабатывает запросы на сохранение статистических данных.
     * Возвращает код 201.
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody StatDto statDto) {
        service.add(statDto);
    }

    /**
     * Обрабатывает запросы предоставления данных.
     * Возвращает код 200.
     */
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatResponseDto> get(@RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                                     @RequestParam (required = false) String[] uris,
                                     @RequestParam (defaultValue = "false") Boolean unique) {
        return service.get(start, end, uris, unique);
    }
}