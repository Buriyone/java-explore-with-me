package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Интерфейс сервиса пользователей.
 */
public interface UserService {
    UserDto add(UserDto userDto);
    List<UserDto> get(int[] ids, int from, int size);
    void delete(int userId);
    User findById(int userId);
    void existsByIds(int[] userIds);

}
