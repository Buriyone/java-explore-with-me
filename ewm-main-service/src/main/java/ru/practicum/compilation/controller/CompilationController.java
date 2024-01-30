package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с подборками событий.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationController {
    /**
     * Предоставляет доступ к сервису подборки событий.
     */
    private final CompilationService compilationService;

    /**
     * Обрабатывает запросы на предоставление списка подборки событий.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return compilationService.getAll(pinned,from, size);
    }

    /**
     * Обрабатывает запросы на предоставление подборки событий по уникальному идентификатору.
     */
    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable int compId) {
        return compilationService.getById(compId);
    }
}
