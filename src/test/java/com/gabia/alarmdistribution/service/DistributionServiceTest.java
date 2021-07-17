package com.gabia.alarmdistribution.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.util.MemoryAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class DistributionServiceTest {

    @MockBean
    private AlarmService alarmService;

    @Autowired
    private DistributionService service;

    private MemoryAppender memoryAppender;

    @BeforeEach
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    public void send_테스트() throws Exception {
        // given
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        Long groupId = 1L;
        Long userId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";
        String traceId = "abc";

        AlarmRequest request = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(alarmService).send(any(), any());

        // when
        service.send(userId, traceId, request);

        // then
        assertThat(memoryAppender.getSize()).isEqualTo(1);
        assertThat(memoryAppender.contains(String.format("%s: userId:%s traceId:%s massage:%s", "DistributionService", userId, traceId, "메세지 적재 완료"), Level.INFO)).isTrue();
    }
}