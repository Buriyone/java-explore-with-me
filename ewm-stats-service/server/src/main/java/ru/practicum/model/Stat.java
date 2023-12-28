package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Сущность подлежащая сохранению в базу данных.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stats")
@Builder(toBuilder = true)
public class Stat {
    /**
     * Идентификационный номер.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Название приложения.
     */
    @Column(name = "app_name")
    @NotBlank(message = "Название приложения не может отсутствовать.")
    private String app;
    /**
     * Идентификатор ресурса.
     */
    @Column(name = "uri_address")
    @NotBlank(message = "Идентификатор ресурса не может отсутствовать.")
    private String uri;
    /**
     * IP адрес.
     */
    @Column(name = "ip_address")
    @NotBlank(message = "ip не может отсутствовать.")
    private String ip;
    /**
     * Время запроса.
     */
    @Column(name = "time_request")
    @NotNull(message = "Время запроса не может отсутствовать.")
    private LocalDateTime timestamp;
}