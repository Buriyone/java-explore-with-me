package ru.practicum.service;

import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс сервиса статистики.
 */
public interface StatService {
    StatDto add(StatDto statDto);

    List<StatResponseDto> get(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
