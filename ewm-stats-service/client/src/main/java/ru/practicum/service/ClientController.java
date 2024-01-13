package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatDto;

import java.time.LocalDateTime;

/**
 * Контроллер HTTP-клиента для обработки статистики.
 */
@RestController
@RequiredArgsConstructor
public class ClientController {
    /**
     * Константа формата даты и времени.
     */
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * Предоставляет доступ к сервису HTTP-клиента.
     */
    private final ClientService service;

    /**
     * Обрабатывает запросы на сохранение статистических данных.
     * Возвращает код 201.
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@RequestBody StatDto statDto) {
        return service.add(statDto);
    }

    /**
     * Обрабатывает запросы предоставления данных.
     * Возвращает код 200.
     */
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> get(@RequestParam @DateTimeFormat(pattern = PATTERN)LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = PATTERN)LocalDateTime end,
                                      @RequestParam (required = false) String[] uris,
                                      @RequestParam (defaultValue = "false") Boolean unique) {
        return service.get(start, end, uris, unique);
    }
}
