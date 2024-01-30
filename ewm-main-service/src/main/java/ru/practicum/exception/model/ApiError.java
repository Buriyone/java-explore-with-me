package ru.practicum.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Модель ошибки.
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApiError {
    /**
     * Сообщение об ошибке.
     */
    private final String message;
    /**
     * Общее описание причины ошибки.
     */
    private final String reason;
    /**
     * Код статуса HTTP-ответа.
     */
    private final String status;
    /**
     * Дата и время когда произошла ошибка.
     */
    private final String timestamp;
}
