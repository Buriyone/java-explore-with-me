package ru.practicum;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Сущность в формате DTO подлежащая сохранению.
 */
@Data
@Builder(toBuilder = true)
public class StatDto {
    /**
     * Уникальный идентификатор.
     */
    private int id;
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
     * IP адрес.
     */
    @NotBlank(message = "ip не может отсутствовать.")
    private String ip;
    /**
     * Время запроса.
     */
    @NotBlank(message = "Время запроса не может отсутствовать.")
    private String timestamp;
}
