package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

/**
 * Интерфейс сервиса категорий.
 */
public interface CategoryService {
    CategoryDto add(CategoryDto categoryDto);

    CategoryDto update(int catId, CategoryDto categoryDto);

    void delete(int catId);

    List<CategoryDto> get(int from, int size);

    CategoryDto getById(int catId);

    Category findById(int catId);

    void existsByIds(int[] categoryIds);
}
