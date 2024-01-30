package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

/**
 * Репозиторий данных категории.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Проверяет наличие категории в базе.
     */
    boolean existsByName(String name);

    /**
     * Проверяет принадлежность названия и уникального идентификатора.
     */
    boolean existsByNameAndIdNot(String name, int id);

    /**
     * Проверяет наличие категорий по списку.
     */
    boolean existsByIdIn(int[] categoryIds);
}
