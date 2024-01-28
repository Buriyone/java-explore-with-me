package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс новой подборки событий в формате DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    /**
     * Список уникальных идентификаторов событий входящих в подборку.
     */
    private List<Integer> events = new ArrayList<>();
    /**
     * Закреплена ли подборка на главной странице сайта.
     */
    private Boolean pinned;
    /**
     * Заголовок подборки.
     */
    @NotBlank(message = "Заголовок подборки не может отсутствовать или быть пустым.")
    @Size(min = 1, max = 50, message = "Количество символов в заголовке должно быть не менее 1 и не более 50.")
    private String title;
}
