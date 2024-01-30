package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Класс подборки событий с измененной информацией о подборке событий в формате DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    /**
     * Список уникальных идентификаторов событий входящих в подборку.
     */
    private List<Integer> events;
    /**
     * Закреплена ли подборка на главной странице сайта.
     */
    private Boolean pinned;
    /**
     * Заголовок подборки.
     */
    @Size(min = 1, max = 50, message = "Количество символов в заголовке должно быть не менее 1 и не более 50.")
    private String title;
}
