package ru.practicum.subscription.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.dto.UserShortDto;

/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    /**
     * Конвертирует {@link Subscription} в {@link SubscriptionDto}.
     */
    @Mapping(target = "user", qualifiedByName = "toUserShortDto")
    @Mapping(target = "subscriber", qualifiedByName = "toUserShortDto")
    SubscriptionDto toSubscriptionDto(Subscription subscription);

    /**
     * Вспомогательный метод маппинга {@link User} в {@link UserShortDto}.
     */
    @Named("toUserShortDto")
    default UserShortDto toUserShortDto(User user) {
        return Mappers.getMapper(UserMapper.class).toUserShortDto(user);
    }
}
