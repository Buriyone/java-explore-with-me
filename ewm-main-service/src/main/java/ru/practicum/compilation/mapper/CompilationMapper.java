package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.List;


/**
 * Маппер сущностей.
 */
@Mapper(componentModel = "spring")
public interface CompilationMapper {
    /**
     * Конвертирует {@link NewCompilationDto} в {@link Compilation}.
     */
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "pinned", defaultValue = "false")
    Compilation toCompilation(NewCompilationDto compilationDto);

    /**
     * Конвертирует {@link Compilation} в {@link CompilationDto}.
     */
    @Mapping(target = "events", qualifiedByName = "toListEventShortDto")
    CompilationDto toCompilationDto(Compilation compilation);

    /**
     * Вспомогательный метод маппинга списка {@link Event} в список {@link EventShortDto}.
     */
    @Named("toListEventShortDto")
    default List<EventShortDto> toListEventShortDto(List<Event> events) {
        return Mappers.getMapper(EventMapper.class).toListEventShortDto(events);
    }
}
