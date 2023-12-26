package ru.practicum;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Сущность в формате DTO подлежащая сохранению.
 */
@Data
@Builder(toBuilder = true)
public class StatDto {
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
     * IP адрес.
     */
    @NotNull(message = "ip не может отсутствовать.")
    private String ip;
    /**
     * Время запроса.
     */
    @NotNull(message = "Время запроса не может отсутствовать.")
    private String timestamp;
}
