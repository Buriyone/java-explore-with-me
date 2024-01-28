package ru.practicum.event.service;

import ru.practicum.assistant.SortOption;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс сервиса событий.
 */
public interface EventService {
    EventFullDto add(NewEventDto newEventDto, int userId);
    List<EventFullDto> getAllByInitiator(int userId, int from, int size);
    EventFullDto getByIdByInitiator(int userId, int eventId);
    EventFullDto updateByIdByInitiator(int userId, int eventId, UpdateEventUserRequest eventUserRequest);
    Event findById(int eventId);
    void ownershipChecker(Event event, int userId);
    EventFullDto getById(int eventId, HttpServletRequest request);
    List<EventShortDto> getEvents(String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, SortOption sort,
                                  int from, int size, HttpServletRequest request);
    List<EventFullDto> getEventsByAdmin(Integer[] users, String[] states, Integer[] categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
    EventFullDto updateByIdByAdmin(int eventId, UpdateEventAdminRequest request);
    List<Event> findAllByIds(List<Integer> ids);
}
