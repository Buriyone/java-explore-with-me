package ru.practicum;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Сущность в формате DTO подлежащая возврату.
 */
@Data
@Builder(toBuilder = true)
public class StatResponseDto {
    /**
     * Название приложения.
     */
    @NotBlank(message = "Название приложения не может отсутствовать.")
    private String app;
    /**
     * Идентификатор ресурса.
     */
    @NotBlank(message = "Идентификатор ресурса не может отсутствовать.")
    private String uri;
    /**
     * Количество запросов.
     */
    private long hits;
}
