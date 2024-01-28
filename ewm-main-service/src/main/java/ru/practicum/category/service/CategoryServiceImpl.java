package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.model.ValidationException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса сервиса категорий.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    /**
     * Предоставляет доступ к хранилищу.
     */
    private final CategoryRepository repository;
    /**
     * Предоставляет доступ к мапперу.
     */
    public final CategoryMapper mapper;
    /**
     * Предоставляет доступ к репозиторию событий.
     */
    private final EventRepository eventRepository;

    /**
     * Сервисный метод регистрации и сохранению новой категории.
     * Генерирует ошибку {@link ConflictException} если название уже занято.
     * @param categoryDto объект категории в формате {@link CategoryDto} подлежащий регистрации и сохранению.
     * @return возвращает зарегистрированный объект категории в формате {@link CategoryDto}.
     */
    @Override
    @Transactional
    public CategoryDto add(CategoryDto categoryDto) {
        log.info("Поступил запрос на добавление категории {}", categoryDto.getName());
        if (categoryChecker(categoryDto.getName())) {
            throw new ConflictException("Название уже занято");
        } else {
            log.info("Категория {} успешно добавлена.", categoryDto.getName());
            return mapper.toCategoryDto(repository.save(mapper.toCategory(categoryDto)));
        }
    }

    /**
     * Сервисный метод обновления данных категорий.
     * Пользуется вспомогательным методом поиска категорий findById().
     * Генерирует ошибку {@link ConflictException} если название уже занято.
     * @param catId уникальный идентификатор категории.
     * @param categoryDto сущность в формате {@link CategoryDto} содержащая данные для обновления.
     * @return возвращает обновленный объект категории в формате {@link CategoryDto}.
     */
    @Override
    @Transactional
    public CategoryDto update(int catId, CategoryDto categoryDto) {
        log.info("Поступил запрос на обновление названия категории {}", categoryDto.getName());
        findById(catId);
        if (repository.existsByNameAndIdNot(categoryDto.getName(), catId)) {
            throw new ConflictException("Название уже занято");
        } else {
            log.info("Категория успешно обновлена.");
            categoryDto.setId(catId);
            return mapper.toCategoryDto(repository.save(mapper.toCategory(categoryDto)));
        }
    }

    /**
     * Сервисный метод удаления категории.
     * Пользуется вспомогательным методом поиска категории для проверки ее наличия.
     * @param catId уникальный идентификатор категории.
     */
    @Override
    @Transactional
    public void delete(int catId) {
        log.info("Поступил запрос на удаление категории с id: {}", catId);
        Category category = findById(catId);
        if (eventRepository.existsByCategory(category)) {
            throw new ConflictException("Невозможно удалить категорию к которой привязано событие.");
        }
        repository.delete(category);
    }

    /**
     * Сервисный метод предоставления категорий постранично.
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора.
     * @param size количество категорий в наборе.
     * @return возвращает итоговый список с категориями в формате {@link CategoryDto}.
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список.
     */
    @Override
    public List<CategoryDto> get(int from, int size) {
        log.info("Поступил запрос на предоставление страницы категорий с {} по {}.", from, size);
        log.info("Страница категорий успешно предоставлена.");
        return repository.findAll(PageRequest.of(from, size)).stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод предоставления категории по уникальному идентификатору.
     * Пользуется вспомогательным методом поиска категории findById().
     * @param catId уникальный идентификатор категории.
     * @return возвращает объект категории в формате {@link CategoryDto}.
     */
    @Override
    public CategoryDto getById(int catId) {
        log.info("Поступил запрос на предоставление категории по уникальному идентификатору.");
        return mapper.toCategoryDto(findById(catId));
    }

    /**
     * Вспомогательный метод поиска категории по уникальному идентификатору.
     * Генерирует {@link ValidationException} если уникальный идентификатор не зарегистрирован.
     * Генерирует {@link NotFoundException} если категория не была найдена.
     * @param catId уникальный идентификатор категории.
     * @return возвращает найденный объект в формате {@link Category}.
     */
    @Override
    public Category findById(int catId) {
        log.debug("Осуществляется поиск категории по id.");
        if (catId == 0) {
            throw new ValidationException("Уникальный идентификатор не зарегистрирован.");
        } else {
            log.debug("Поиск категории по id завершен.");
            return repository.findById(catId)
                    .orElseThrow(() -> new NotFoundException(String.format("Категория с id: %d не найдена.", catId)));
        }
    }

    /**
     * Проверяет наличие категорий по списку.
     * Генерирует {@link ValidationException} если один или несколько уникальных идентификаторов не зарегистрировано.
     * Генерирует {@link NotFoundException} если одна или несколько категорий не найдены.
     * @param categoryIds список уникальных идентификаторов категорий.
     */
    @Override
    public void existsByIds(int[] categoryIds) {
        log.debug("Осуществляется проверка категорий из списка.");
        for (Integer id : categoryIds) {
            if(id == 0) {
                throw new ValidationException("Один или несколько уникальных идентификаторов не зарегистрировано.");
            }
        }
        if (!repository.existsByIdIn(categoryIds)) {
            throw new NotFoundException("Одна или несколько категорий не найдены.");
        }
    }

    /**
     * Сервисный метод проверки наличия названия категории в системе.
     * @param name название категории подлежащее проверке.
     * @return возвращает true если название категории уже занято, false если свободно.
     */
    private boolean categoryChecker(String name) {
        return repository.existsByName(name);
    }
}
