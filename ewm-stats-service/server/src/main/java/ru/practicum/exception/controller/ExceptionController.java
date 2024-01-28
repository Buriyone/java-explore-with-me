package ru.practicum.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.model.ApiError;
import ru.practicum.exception.model.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.controller.StatController.PATTERN;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    /**
     * Отлавливает ошибку {@link ValidationException}
     *
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
}
