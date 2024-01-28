package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с категориями.
 */
@RestController
@RequiredArgsConstructor
public class CategoryController {
    /**
     * Предоставляет доступ к сервису категорий.
     */
    private final CategoryService service;

    /**
     * Обрабатывает запросы на регистрацию и сохранение категорий.
     * Возвращает код 201.
     */
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody CategoryDto categoryDto) {
        return service.add(categoryDto);
    }

    /**
     * Обрабатывает запросы на обновление данных в категориях.
     * Возвращает код 200.
     */
    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable int catId,
                              @Valid @RequestBody CategoryDto categoryDto) {
        return service.update(catId, categoryDto);
    }

    /**
     * Обрабатывает запросы на удаление категорий.
     * Возвращает код 204.
     */
    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int catId) {
        service.delete(catId);
    }

    /**
     * Обрабатывает запросы на предоставление категорий по параметрам.
     * Возвращает код 200.
     */
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> get(@RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        return service.get(from, size);
    }

    /**
     * Обрабатывает запросы на предоставление категорий по уникальному идентификатору.
     * Возвращает код 200.
     */
    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable int catId) {
        return service.getById(catId);
    }
}