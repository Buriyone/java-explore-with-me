package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Сущность в формате DTO подлежащая возврату.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private int hits;
}
