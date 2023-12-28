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
import ru.practicum.client.BaseClient;

import java.time.LocalDateTime;
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
    public ResponseEntity<Object> add(StatDto statDto) {
        log.info("Поступил POST запрос на передачу данных подлежащих сохранению.");
        return post("/hit", statDto);
    }

    /**
     * Отправляет GET запрос передавая параметры по которым производится поиск и предоставление данных.
     */
    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        log.info("Поступил GET запрос на передачу и возврат данных по параметрам.");
        Map<String, Object> param = Map.of("start", start, "end", end, "uris", uris, "unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", param);
    }
}
