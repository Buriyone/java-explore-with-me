package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;

/**
 * Репозиторий подборки событий.
 */
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    /**
     * Проверяет наличие подборки по существующим заголовкам.
     */
    boolean existsByTitle(String title);

    /**
     * Предоставляет список подборок с закреплением.
     */
    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
