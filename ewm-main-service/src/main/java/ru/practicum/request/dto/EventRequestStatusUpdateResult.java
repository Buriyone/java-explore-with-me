package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Класс результата подтверждения/отклонения заявок на участие в событии.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventRequestStatusUpdateResult {
    /**
     * Список подтвержденных заявок.
     */
    private List<ParticipationRequestDto> confirmedRequests;
    /**
     * Список отклоненных заявок.
     */
    private List<ParticipationRequestDto> rejectedRequests;
}
