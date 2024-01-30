package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    /**
     * Конвертирует {@link CategoryDto} в {@link Category}.
     */
    Category toCategory(CategoryDto categoryDto);

    /**
     * Конвертирует {@link Category} в {@link CategoryDto}.
     */
    CategoryDto toCategoryDto(Category category);
}
