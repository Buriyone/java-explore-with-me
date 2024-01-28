package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.model.ValidationException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса сервиса пользователей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * Предоставляет доступ к хранилищу.
     */
    private final UserRepository repository;
    /**
     * Предоставляет доступ к мапперу.
     */
    private final UserMapper mapper;

    /**
     * Сервисный метод регистрации и сохранения пользователя.
     * Генерирует {@link ConflictException} если имя пользователя или электронная почта уже заняты.
     * @param userDto объект пользователя в формате {@link UserDto}
     * @return возвращает зарегистрированного пользователя в формате {@link UserDto}.
     */
    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        log.info("Поступил запрос на регистрацию и добавление пользователя.");
        if (repository.existsByName(userDto.getName())) {
            throw new ConflictException("Имя пользователя уже занято.");
        } else if (repository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Электронная почта уже занята.");
        } else {
            log.info("Пользователь успешно зарегистрирован и сохранен.");
            return mapper.toUserDto(repository.save(mapper.toUser(userDto)));
        }
    }

    /**
     * Сервисный метод предоставления списка пользователей с возможностью перечисления и постраничного вывода.
     * @param ids перечисление необходимых уникальных идентификаторов.
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора.
     * @param size количество элементов в наборе.
     * @return возвращает список пользователей в зависимости от заданных параметров.
     */
    @Override
    public List<UserDto> get(int[] ids, int from, int size) {
        log.info("Поступил запрос на предоставление пользователей из списка уникальных идентификаторов.");
        if (ids == null) {
            log.info("Общий список пользователей предоставлен.");
            return repository.findAll(PageRequest.of(from, size)).stream()
                    .map(mapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            log.info("Список пользователей по уникальным идентификаторам предоставлен.");
            return repository.findAllByIdIn(ids, PageRequest
                            .of(from, size, Sort.by(Sort.Direction.DESC, "id")))
                    .stream()
                    .map(mapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Сервисный метод удаления пользователя по уникальному идентификатору.
     * Пользуется вспомогательным методом findById для осуществления валидации и поиска пользователя.
     * @param userId уникальный идентификатор пользователя.
     */
    @Override
    @Transactional
    public void delete(int userId) {
        log.info("Поступил запрос на удаление пользователя с id: {}.", userId);
        repository.delete(findById(userId));
    }

    /**
     * Сервисный метод поиска и предоставления пользователя по уникальному идентификатору.
     * Генерирует {@link ValidationException} если уникальный идентификатор не был зарегистрирован.
     * Генерирует {@link NotFoundException} если пользователь не найден.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает пользователя если он есть в базе.
     */
    @Override
    public User findById(int userId) {
        log.debug("Осуществляется поиск пользователя по уникальному идентификатору.");
        if (userId == 0) {
            throw new ValidationException("Уникальный идентификатор не зарегистрирован.");
        } else {
            return repository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        }
    }

    /**
     * Проверяет наличие пользователей по списку.
     * Генерирует {@link ValidationException} если один или несколько уникальных идентификаторов не зарегистрировано.
     * Генерирует {@link NotFoundException} если Один или несколько пользователей не найдены.
     * @param userIds список уникальных идентификаторов пользователей.
     */
    @Override
    public void existsByIds(int[] userIds) {
        log.debug("Осуществляется проверка пользователей из списка.");
        for (Integer id : userIds) {
            if(id == 0) {
                throw new ValidationException("Один или несколько уникальных идентификаторов не зарегистрировано.");
            }
        }
        if (!repository.existsByIdIn(userIds)) {
            throw new NotFoundException("Один или несколько пользователей не найдены.");
        }
    }
}