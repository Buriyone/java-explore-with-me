package ru.practicum.exception.model;

/**
 * Ошибка конфликта. Возникает если данные уже были заняты.
 * Код ошибки 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
