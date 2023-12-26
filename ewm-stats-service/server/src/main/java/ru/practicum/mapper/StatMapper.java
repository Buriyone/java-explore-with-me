package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;
import ru.practicum.model.Stat;
import ru.practicum.model.StatResponse;

import static ru.practicum.controller.StatController.PATTERN;

/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface StatMapper {
    /**
     * Конвертирует {@link StatDto} в {@link Stat}.
     */
    @Mapping(target = "timestamp", dateFormat = PATTERN)
    Stat toStat(StatDto statDto);

    /**
     * Конвертирует {@link StatResponse} в {@link StatResponseDto}.
     */
    StatResponseDto toStatResponseDto(StatResponse statResponse);
}
