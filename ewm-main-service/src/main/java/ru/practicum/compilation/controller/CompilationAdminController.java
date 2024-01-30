package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

/**
 * Контроллер сервера для обработки запросов администратора связанных с подборками событий.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    /**
     * Предоставляет доступ к сервису подборки событий.
     */
    private final CompilationService compilationService;

    /**
     * Обрабатывает запросы на регистрацию и сохранение подборки событий.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.add(compilationDto);
    }

    /**
     * Обрабатывает запросы на обновление данных подборки событий.
     */
    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable int compId,
                                 @Valid @RequestBody UpdateCompilationRequest request) {
        return compilationService.update(compId, request);
    }

    /**
     * Обрабатывает запросы на удаление подборки событий.
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int compId) {
        compilationService.delete(compId);
    }
}
