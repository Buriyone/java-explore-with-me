package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с подборками событий.
 */
@RestController
@RequiredArgsConstructor
public class CompilationController {
    /**
     * Предоставляет доступ к сервису подборки событий.
     */
    private final CompilationService compilationService;

    /**
     * Обрабатывает запросы на регистрацию и сохранение подборки событий.
     */
    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.add(compilationDto);
    }

    /**
     * Обрабатывает запросы на обновление данных подборки событий.
     */
    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable int compId,
                                 @Valid @RequestBody UpdateCompilationRequest request) {
        return compilationService.update(compId, request);
    }

    /**
     * Обрабатывает запросы на удаление подборки событий.
     */
    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int compId) {
        compilationService.delete(compId);
    }

    /**
     * Обрабатывает запросы на предоставление списка подборки событий.
     */
    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return compilationService.getAll(pinned,from, size);
    }

    /**
     * Обрабатывает запросы на предоставление подборки событий по уникальному идентификатору.
     */
    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable int compId) {
        return compilationService.getById(compId);
    }
}
