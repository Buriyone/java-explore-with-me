package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс заявки на участие в событии в формате DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipationRequestDto {
    /**
     * Уникальный идентификатор заявки.
     */
    private int id;
    /**
     * Дата и время создания заявки.
     */
    private String created;
    /**
     * Уникальный идентификатор события.
     */
    private int event;
    /**
     * Уникальный идентификатор пользователя, отправившего заявку.
     */
    private int requester;
    /**
     * Статус заявки.
     */
    private String status;
}
