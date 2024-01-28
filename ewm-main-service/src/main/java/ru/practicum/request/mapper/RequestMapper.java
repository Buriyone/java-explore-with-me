package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.ParticipationRequest;

import static ru.practicum.event.controller.EventController.PATTERN;

/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface RequestMapper {
    /**
     * Конвертирует {@link ParticipationRequestDto} в {@link ParticipationRequest}.
     */
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "created", dateFormat = PATTERN)
    ParticipationRequest toParticipationRequest(ParticipationRequestDto requestDto);

    /**
     * Конвертирует {@link ParticipationRequest} в {@link ParticipationRequestDto}.
     */
    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    @Mapping(target = "created", dateFormat = PATTERN)
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request);
}
