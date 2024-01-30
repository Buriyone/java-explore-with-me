package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с категориями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryController {
    /**
     * Предоставляет доступ к сервису категорий.
     */
    private final CategoryService service;

    /**
     * Обрабатывает запросы на предоставление категорий по параметрам.
     * Возвращает код 200.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> get(@RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        return service.get(from, size);
    }

    /**
     * Обрабатывает запросы на предоставление категорий по уникальному идентификатору.
     * Возвращает код 200.
     */
    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable int catId) {
        return service.getById(catId);
    }
}