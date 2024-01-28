package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс подборки событий.
 */
@Data
@Entity
@Table(name = "compilations")
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    /**
     * Уникальный идентификатор подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Список событий входящих в подборку.
     */
    @ManyToMany
    private List<Event> events = new ArrayList<>();
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
