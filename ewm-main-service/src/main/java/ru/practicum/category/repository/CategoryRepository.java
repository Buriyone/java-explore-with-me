package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

import java.util.Optional;

/**
 * Репозиторий данных категории.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Проверяет наличие категории в базе.
     */
    boolean existsByName(String name);

    /**
     * Находит и возвращает категорию по уникальному идентификатору.
     */
    Optional<Category> findById(int id);

    /**
     * Проверяет принадлежность названия и уникального идентификатора.
     */
    boolean existsByNameAndIdNot(String name, int id);

    /**
     * Проверяет наличие категорий по списку.
     */
    boolean existsByIdIn(int[] categoryIds);
}
