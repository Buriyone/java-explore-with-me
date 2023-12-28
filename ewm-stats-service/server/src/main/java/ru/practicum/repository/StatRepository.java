package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Stat;
import ru.practicum.model.StatResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий статистических данных.
 */
public interface StatRepository extends JpaRepository<Stat, Long> {
    /**
     * Возвращает все данные без учета идентификатора ресурса, преобразовывает в {@link StatResponse}.
     */
    @Query(value = "SELECT new ru.practicum.model.StatResponse(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatResponse> getAllStat(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

    /**
     * Возвращает уникальные данные без учета идентификатора ресурса, преобразовывает в {@link StatResponse}.
     */
    @Query(value = "SELECT new ru.practicum.model.StatResponse(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp between :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatResponse> getUniqueStat(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    /**
     * Возвращает все данные с учетом идентификатора ресурса, преобразовывает в {@link StatResponse}.
     */
    @Query(value = "SELECT new ru.practicum.model.StatResponse(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatResponse> getAllStatWithUri(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("uris") String[] uris);

    /**
     * Возвращает уникальные данные с учетом идентификатора ресурса, преобразовывает в {@link StatResponse}.
     */
    @Query(value = "SELECT new ru.practicum.model.StatResponse(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp between :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatResponse> getUniqueStatWithUri(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end,
                                            @Param("uris") String[] uris);
}
