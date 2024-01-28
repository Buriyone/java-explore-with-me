package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.model.ValidationException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса сервиса подборки событий.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    /**
     * Предоставляет доступ к репозиторию подборки событий.
     */
    private final CompilationRepository compilationRepository;
    /**
     * Предоставляет доступ к мапперу.
     */
    private final CompilationMapper compilationMapper;
    /**
     * Предоставляет доступ к сервису событий.
     */
    private final EventService eventService;

    /**
     * Сервисный метод регистрации и сохранения подборки событий.
     * Генерирует {@link ConflictException} если заголовок подборки событий уже занят.
     * @param compilationDto новая подборка событий в формате {@link NewCompilationDto}.
     * @return возвращает зарегистрированную подборку событий в формате {@link CompilationDto}.
     */
    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto compilationDto) {
        log.info("Поступил запрос на регистрацию и сохранение подборки событий.");
        if (titleValidation(compilationDto.getTitle())) {
            throw new ConflictException("Подборка событий с таким заголовком уже существует.");
        }
        Compilation compilation = compilationMapper.toCompilation(compilationDto);
        compilation.setEvents(eventService.findAllByIds(compilationDto.getEvents()));
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));

    }

    /**
     * Сервисный метод обновления подборки событий по уникальному идентификатору.
     * Генерирует {@link ConflictException} если заголовок подборки событий уже занят.
     * @param compId уникальный идентификатор события.
     * @param request данные для обновления в формате {@link UpdateCompilationRequest}.
     * @return возвращает обновленные данные подборки событий в формате {@link CompilationDto}.
     */
    @Override
    @Transactional
    public CompilationDto update(int compId, UpdateCompilationRequest request) {
        log.info("Поступил запрос на обновление данных подборки событий.");
        Compilation compilation = findById(compId);
        if(request.getEvents() != null) {
            compilation.setEvents(eventService.findAllByIds(request.getEvents()));
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            if (!request.getTitle().toUpperCase().equals(compilation.getTitle())
                    && titleValidation(request.getTitle())) {
                throw new ConflictException("Подборка событий с таким заголовком уже существует.");
            } else {
                compilation.setTitle(request.getTitle());
            }
        }
        return compilationMapper.toCompilationDto(compilation);
    }

    /**
     * Сервисный метод удаления подборки событий по уникальному идентификатору.
     * @param compId уникальный идентификатор подборки событий.
     */
    @Override
    @Transactional
    public void delete(int compId) {
        log.info("Поступил запрос на удаление подборки событий по уникальному идентификатору: {}.", compId);
        compilationRepository.delete(findById(compId));
    }

    /**
     * Сервисный метод предоставления списка подборок событий по параметрам.
     * @param pinned параметр закрепления события на главной странице сайта.
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора.
     * @param size количество элементов в наборе.
     * @return возвращает список подборок событий в формате {@link CompilationDto}.
     */
    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        log.info("Поступил запрос на предоставление списка подборки событий по параметрам.");
        if (pinned != null && pinned.equals(true)) {
            return compilationRepository
                    .findAllByPinned(true, PageRequest.of(from, size,
                            Sort.by(Sort.Direction.ASC, "id")))
                    .stream().map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(PageRequest.of(from, size,
                            Sort.by(Sort.Direction.ASC, "id")))
                    .stream()
                    .map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Сервисный метод предоставления подборки событий по уникальному идентификатору.
     * @param compId уникальный идентификатор подборки событий.
     * @return возвращает подборку событий в формате {@link CompilationDto}.
     */
    @Override
    public CompilationDto getById(int compId) {
        log.info("Поступил запрос на предоставление подборки событий по уникальному идентификатору: {}.", compId);
        return compilationMapper.toCompilationDto(findById(compId));
    }

    /**
     * Вспомогательный метод поиска и предоставления подборки событий по уникальному идентификатору.
     * @param compId уникальный идентификатор подборки событий.
     * @return возвращает подборку событий в формате {@link Compilation}.
     */
    private Compilation findById(int compId) {
        log.debug("Осуществляется поиск подборки событий по идентификатору: {}", compId);
        if (compId == 0) {
            throw new ValidationException("Уникальный идентификатор не зарегистрирован.");
        } else {
            return compilationRepository.findById(compId)
                    .orElseThrow(() -> new NotFoundException("Подборка событий не найдена."));
        }
    }

    /**
     * Вспомогательный метод проверки наличия подборки по заголовку.
     * @param title заголовок подборки событий.
     * @return возвращает true если подборка существует, false если заголовок свободен.
     */
    private boolean titleValidation(String title) {
        log.debug("Осуществляется проверка наличия подборки по заголовку.");
        return compilationRepository.existsByTitle(title);
    }
}
