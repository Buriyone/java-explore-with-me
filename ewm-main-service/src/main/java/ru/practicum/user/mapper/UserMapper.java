package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Конвертирует {@link UserDto} в {@link User}.
     */
    User toUser(UserDto userDto);

    /**
     * Конвертирует {@link User} в {@link UserDto}.
     */
    UserDto toUserDto(User user);

    /**
     * Конвертирует {@link User} в {@link UserShortDto}.
     */
    UserShortDto toUserShortDto(User user);
}