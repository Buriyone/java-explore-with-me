package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.ParticipationRequest;

import java.util.List;

/**
 * Интерфейс сервиса заявок.
 */
public interface RequestService {
    ParticipationRequestDto add(int userId, int eventId);

    List<ParticipationRequestDto> get(int userId);

    ParticipationRequestDto cancel(int userId, int requestId);

    List<ParticipationRequestDto> getByInitiator(int userId, int eventId);

    ParticipationRequest findById(int requestId);

    EventRequestStatusUpdateResult moderation(int userId,
                                              int eventId,
                                              EventRequestStatusUpdateRequest request);
}
