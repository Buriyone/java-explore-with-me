package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер сервера для обработки запросов связанных с пользователями.
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * Предоставляет доступ к сервису категорий.
     */
    private final UserService service;

    /**
     * Обрабатывает запросы на регистрацию и сохранение пользователей.
     * Возвращает код 201.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return service.add(userDto);
    }

    /**
     * Обрабатывает запросы на предоставление пользователей.
     * Возвращает код 200.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> get(@RequestParam(required = false) int[] ids,
                             @RequestParam(defaultValue = "0") int from,
                             @RequestParam(defaultValue = "10") int size) {
        return service.get(ids, from, size);
    }

    /**
     * Обрабатывает запросы на удаление пользователей.
     * Возвращает код 204.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int userId) {
        service.delete(userId);
    }
}
