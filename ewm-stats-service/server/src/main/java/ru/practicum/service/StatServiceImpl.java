package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;
import ru.practicum.exception.model.ValidationException;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса сервиса статистики.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    /**
     * Предоставляет доступ к репозиторию.
     */
    private final StatRepository statRepository;
    /**
     * Предоставляет доступ к {@link StatMapper}.
     */
    private final StatMapper mapper;

    /**
     * Принимает DTO-объект, конвертирует в {@link Stat} и сохраняет в базу данных.
     * @param statDto принимаемый DTO-объект.
     */
    @Override
    @Transactional
    public StatDto add(StatDto statDto) {
        log.info("Поступил запрос на сохранение данных.");
        log.info("Данные успешно сохранены.");
        return mapper.toStatDto(statRepository.save(mapper.toStat(statDto)));
    }

    /**
     * Возвращает список состоящий из {@link StatResponseDto} удовлетворяющий параметрам запроса.
     * @param start начальное время запроса.
     * @param end конечное время запроса.
     * @param uris массив идентификаторов ресурса запроса.
     * @param unique уникальность данных в списке возврата.
     */
    @Override
    public List<StatResponseDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        log.info("Поступил запрос на предоставление статистики.");
        log.info("Данные успешно предоставлены.");
        if (end.isAfter(start)) {
            throw new ValidationException("Конечное время не может быть раньше начального.");
        }
        return (uris == null || uris.length == 0 ?
                (unique ? statRepository.getUniqueStat(start, end)
                        : statRepository.getAllStat(start, end))
                : (unique ? statRepository.getUniqueStatWithUri(start, end, uris)
                : statRepository.getAllStatWithUri(start, end, uris)))
                .stream()
                .map(mapper::toStatResponseDto)
                .collect(Collectors.toList());
    }
}