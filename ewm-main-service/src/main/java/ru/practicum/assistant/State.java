package ru.practicum.assistant;

/**
 * Класс перечислений статусов.
 */
public enum State {
    PENDING,
    PUBLISHED,
    CANCELED,
    CONFIRMED,
    REJECTED,
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    PUBLISH_EVENT,
    REJECT_EVENT
}