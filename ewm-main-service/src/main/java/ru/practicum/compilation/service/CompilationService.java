package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

/**
 * Интерфейс сервиса подборки событий.
 */
public interface CompilationService {
    CompilationDto add(NewCompilationDto compilationDto);

    CompilationDto update(int compId, UpdateCompilationRequest request);

    void delete(int compId);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(int compId);
}
