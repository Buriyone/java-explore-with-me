package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Класс географических координат.
 */
@Data
@Entity
@Table(name = "locations")
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    /**
     * Уникальный идентификатор локации.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Координат широты.
     */
    @NotNull(message = "Координат широты не может отсутствовать.")
    private Float lat;
    /**
     * Координат долготы.
     */
    @NotNull(message = "Координат долготы не может отсутствовать.")
    private Float lon;
}
