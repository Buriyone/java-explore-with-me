package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Location;

/**
 * Репозиторий данных координат.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
}
