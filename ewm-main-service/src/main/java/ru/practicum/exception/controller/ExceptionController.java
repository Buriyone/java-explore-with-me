package ru.practicum.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.event.controller.EventController.PATTERN;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    /**
     * Отлавливает ошибку {@link ConflictException}
     * @return возвращает сведения об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflictException(final ConflictException exception) {
        log.info("Конфликт запроса. {}", exception.getMessage());
        return ApiError.builder()
                .message(exception.getMessage())
                .reason("Конфликт запроса.")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    /**
     * Отлавливает ошибку {@link ValidationException}
     * @return возвращает сведения об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validationException(final ValidationException exception) {
        log.info("Ошибка валидации. {}", exception.getMessage());
        return ApiError.builder()
                .message(exception.getMessage())
                .reason("Ошибка валидации.")
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    /**
     * Отлавливает ошибку {@link NotFoundException}.
     * @return возвращает сведения об ошибке.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundException(final NotFoundException exception) {
        log.info("Ошибка поиска. {}", exception.getMessage());
        return ApiError.builder()
                .message(exception.getMessage())
                .reason("Ошибка поиска.")
                .status("NOT_FOUND")
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
