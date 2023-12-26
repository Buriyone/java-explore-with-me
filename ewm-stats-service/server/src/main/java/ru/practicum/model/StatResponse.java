package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Сущность подлежащая извлечению из базы данных.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StatResponse {
    /**
     * Название приложения.
     */
    @NotNull(message = "Название приложения не может отсутствовать.")
    private String app;
    /**
     * Идентификатор ресурса.
     */
    @NotNull(message = "Идентификатор ресурса не может отсутствовать.")
    private String uri;
    /**
     * Количество запросов.
     */
    private long hits;
}
