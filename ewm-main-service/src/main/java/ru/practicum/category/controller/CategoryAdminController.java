package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

/**
 * Контроллер сервера для обработки запросов администратора связанных с категориями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    /**
     * Предоставляет доступ к сервису категорий.
     */
    private final CategoryService service;

    /**
     * Обрабатывает запросы на регистрацию и сохранение категорий.
     * Возвращает код 201.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody CategoryDto categoryDto) {
        return service.add(categoryDto);
    }

    /**
     * Обрабатывает запросы на обновление данных в категориях.
     * Возвращает код 200.
     */
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable int catId,
                              @Valid @RequestBody CategoryDto categoryDto) {
        return service.update(catId, categoryDto);
    }

    /**
     * Обрабатывает запросы на удаление категорий.
     * Возвращает код 204.
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int catId) {
        service.delete(catId);
    }
}
