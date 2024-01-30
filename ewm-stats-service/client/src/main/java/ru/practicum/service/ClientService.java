package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;
import ru.practicum.client.BaseClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис HTTP-клиента.
 */
@Service
@Slf4j
public class ClientService extends BaseClient {
    /**
     * Константа префикса.
     */
    private static final String API_PREFIX = "/";
    /**
     * Паттерн формата времени.
     */
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * Форматтер времени.
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    /**
     * Конструктор сервиса.
     */
    @Autowired
    public ClientService(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    /**
     * Отправляет POST запрос передавая данные подлежащие сохранению.
     */
    public StatDto add(StatDto statDto) {
        log.info("Поступил POST запрос на передачу данных подлежащих сохранению.");
        ResponseEntity<StatDto> response = post("/hit", statDto, new StatDto());
        return response.getBody();
    }

    /**
     * Отправляет GET запрос передавая параметры по которым производится поиск и предоставление данных.
     */
    public List<StatResponseDto> get(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        log.info("Поступил GET запрос на передачу и возврат данных по параметрам.");
        Map<String, Object> parameters = Map.of("start", start.format(formatter), "end", end.format(formatter),
                "uris", uris, "unique", unique);
        ResponseEntity<ArrayList<StatResponseDto>> response
                = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters, new ArrayList<>());
        return response.getBody();
    }
}
