package ru.practicum.event.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.util.List;

import static ru.practicum.event.controller.EventController.PATTERN;

/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {
    /**
     * Конвертирует {@link NewEventDto} в {@link Event}.
     */
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "eventDate", dateFormat = PATTERN)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "paid", defaultValue = "false")
    @Mapping(target = "participantLimit", defaultValue = "0")
    @Mapping(target = "requestModeration", defaultValue = "true")
    Event toEvent(NewEventDto eventDto);

    /**
     * Конвертирует {@link Event} в {@link EventFullDto}.
     */
    @Mapping(target = "category", qualifiedByName = "toCategoryDto")
    @Mapping(target = "initiator", qualifiedByName = "toUserShortDto")
    @Mapping(target = "createdOn", dateFormat = PATTERN)
    @Mapping(target = "eventDate", dateFormat = PATTERN)
    @Mapping(target = "publishedOn", dateFormat = PATTERN)
    EventFullDto toEventFullDto(Event event);

    /**
     * Конвертирует {@link Event} в {@link EventShortDto}.
     */
    @Mapping(target = "category", qualifiedByName = "toCategoryDto")
    @Mapping(target = "initiator", qualifiedByName = "toUserShortDto")
    @Mapping(target = "eventDate", dateFormat = PATTERN)
    @Named("toEventShortDto")
    EventShortDto toEventShortDto(Event event);

    /**
     * Конвертирует {@link UpdateEventAdminRequest} в {@link UpdateEventUserRequest}.
     */
    UpdateEventUserRequest toUpdateEventUserRequest(UpdateEventAdminRequest request);

    /**
     * Конвертирует список в формате {@link Event} в список {@link EventShortDto}.
     */
    @IterableMapping(qualifiedByName = "toEventShortDto")
    List<EventShortDto> toListEventShortDto(List<Event> events);

    /**
     * Вспомогательный метод маппинга {@link Category} в {@link CategoryDto}.
     */
    @Named("toCategoryDto")
    default CategoryDto toCategoryDto(Category category) {
        return Mappers.getMapper(CategoryMapper.class).toCategoryDto(category);
    }

    /**
     * Вспомогательный метод маппинга {@link User} в {@link UserShortDto}.
     */
    @Named("toUserShortDto")
    default UserShortDto toUserShortDto(User user) {
        return Mappers.getMapper(UserMapper.class).toUserShortDto(user);
    }
}
