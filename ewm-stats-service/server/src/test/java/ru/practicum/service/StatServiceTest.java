package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.StatDto;
import ru.practicum.StatResponseDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class StatServiceTest {
    private final StatService service;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime currentTime = LocalDateTime.now().withNano(0);

    private StatDto statDto = StatDto.builder()
            .app("ewm-main-service")
            .ip("192.168.1.1")
            .uri("/stats/1")
            .timestamp(formatter.format(currentTime))
            .build();

    @Test
    public void addTest() {
        service.add(statDto);
        StatResponseDto testDto = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                new String[]{"/stats/1"}, false).get(0);
        assertEquals(1, testDto.getHits(), "Количество запросов отличается.");
        assertEquals(statDto.getApp(), testDto.getApp(), "Имя сервиса отличается.");
        assertEquals(statDto.getUri(), testDto.getUri(), "Идентификатор ресурса отличается.");
    }

    @Test
    public void getAllTest() {
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/1")
                .timestamp(formatter.format(currentTime))
                .build();
        service.add(statDto);
        StatResponseDto testDto = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                null, false).get(0);
        assertEquals(1, testDto.getHits(), "Количество запросов отличается.");
        assertEquals(statDto.getApp(), testDto.getApp(), "Имя сервиса отличается.");
        assertEquals(statDto.getUri(), testDto.getUri(), "Идентификатор ресурса отличается.");
        List<StatResponseDto> testList = service.get(currentTime.plusHours(1), currentTime.plusHours(2),
                null, false);
        assertTrue(testList.isEmpty(), "Время не было учтено.");
    }

    @Test
    public void getUniqueTest() {
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/1")
                .timestamp(formatter.format(currentTime))
                .build();
        service.add(statDto);
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/1")
                .timestamp(formatter.format(currentTime.plusMinutes(1)))
                .build();
        service.add(statDto);
        StatResponseDto testDto = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                null, false).get(0);
        assertEquals(2, testDto.getHits(), "Количество запросов отличается.");
        testDto = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                null, true).get(0);
        assertEquals(1, testDto.getHits(), "Уникальность не была учтена.");
        assertEquals(statDto.getApp(), testDto.getApp(), "Имя сервиса отличается.");
        assertEquals(statDto.getUri(), testDto.getUri(), "Идентификатор ресурса отличается.");
        List<StatResponseDto> testList = service.get(currentTime.plusHours(1), currentTime.plusHours(2),
                null, true);
        assertTrue(testList.isEmpty(), "Время не было учтено.");
    }

    @Test
    public void getAllWithUriTest() {
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/1")
                .timestamp(formatter.format(currentTime))
                .build();
        service.add(statDto);
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/2")
                .timestamp(formatter.format(currentTime.plusMinutes(1)))
                .build();
        service.add(statDto);
        List<StatResponseDto> testList = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                new String[]{"/test/imposter"}, false);
        assertTrue(testList.isEmpty(), "Идентификатор ресурса не был учтен.");
        testList = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                new String[]{statDto.getUri()}, false);
        assertEquals(1, testList.size(), "Идентификатор ресурса учтен некорректно.");
        StatResponseDto testDto = testList.get(0);
        assertEquals(statDto.getApp(), testDto.getApp(), "Имя сервиса отличается.");
        assertEquals(statDto.getUri(), testDto.getUri(), "Идентификатор ресурса отличается.");
        assertEquals(1, testDto.getHits(), "Количество запросов отличается.");
        testList = service.get(currentTime.plusHours(1), currentTime.plusHours(2),
                new String[]{statDto.getUri()}, false);
        assertTrue(testList.isEmpty(), "Время не было учтено.");
    }

    @Test
    public void getUniqueWithUriTest() {
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/1")
                .timestamp(formatter.format(currentTime))
                .build();
        service.add(statDto);
        statDto = StatDto.builder()
                .app("test-service")
                .ip("192.168.1.1")
                .uri("/test/1")
                .timestamp(formatter.format(currentTime.plusMinutes(1)))
                .build();
        service.add(statDto);
        List<StatResponseDto> testList = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                new String[]{"/test/imposter"}, true);
        assertTrue(testList.isEmpty(), "Идентификатор ресурса не был учтен.");
        testList = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                new String[]{statDto.getUri()}, false);
        assertEquals(2, testList.get(0).getHits(), "Количество запросов отличается.");
        StatResponseDto testDto = service.get(currentTime.minusMinutes(30), currentTime.plusMinutes(30),
                new String[]{statDto.getUri()}, true).get(0);
        assertEquals(statDto.getApp(), testDto.getApp(), "Имя сервиса отличается.");
        assertEquals(statDto.getUri(), testDto.getUri(), "Идентификатор ресурса отличается.");
        assertEquals(1, testDto.getHits(), "Уникальность не была учтена.");
        testList = service.get(currentTime.plusHours(1), currentTime.plusHours(2),
                new String[]{statDto.getUri()}, true);
        assertTrue(testList.isEmpty(), "Время не было учтено.");
    }
}