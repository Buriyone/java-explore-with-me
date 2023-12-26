package ru.practicum;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Сущность в формате DTO подлежащая возврату.
 */
@Data
@Builder(toBuilder = true)
public class StatResponseDto {
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
