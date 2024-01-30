package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.assistant.State;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Класс изменение статуса запроса на участие в событии текущего пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventRequestStatusUpdateRequest {
    /**
     * Список уникальных идентификаторов заявок подлежащих обновлению статуса.
     */
    @NotNull(message = "Список идентификаторов не может отсутствовать.")
    private List<Integer> requestIds;
    /**
     * Новый статус запроса на участие в событии.
     */
    @NotNull(message = "Статус не может отсутствовать.")
    private State status;
}
