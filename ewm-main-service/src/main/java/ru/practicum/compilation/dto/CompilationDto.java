package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс подборки событий в формате DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    /**
     * Уникальный идентификатор подборки.
     */
    private int id;
    /**
     * Список событий входящих в подборку.
     */
    @NotNull(message = "Список подборки событий не может отсутствовать.")
    private List<EventShortDto> events = new ArrayList<>();
    /**
     * Закреплена ли подборка на главной странице сайта.
     */
    @NotNull(message = "Факт закрепления подборки не может отсутствовать.")
    private Boolean pinned;
    /**
     * Заголовок подборки.
     */
    @NotBlank(message = "Заголовок подборки не может отсутствовать или быть пустым.")
    private String title;
}
