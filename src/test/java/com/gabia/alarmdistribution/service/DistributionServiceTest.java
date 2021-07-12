package com.gabia.alarmdistribution.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.util.MemoryAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

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
    public void send_테스트() {
        // given
        List<String> slackAddress = Arrays.asList("T13DA561", "U13DA561", "C13DA561");
        List<String> smsAddress = Arrays.asList("01012344321", "01037826481", "01027594837");
        List<String> emailAddress = Arrays.asList("test@gmail.com", "tes1@naver.com", "tes1@naver.com");

        Raw slackRaw = Raw.createRaw("slack", slackAddress);
        Raw emailRaw = Raw.createRaw("email", emailAddress);
        Raw smsRaw = Raw.createRaw("sms", smsAddress);

        List<Raw> raws = Arrays.asList(slackRaw, smsRaw, emailRaw);

        Long groupId = 1L;
        Long userId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";
        String traceId = "abc";

        AlarmRequest request = AlarmRequest.builder()
                .groupId(groupId)
                .userId(userId)
                .title(title)
                .content(content)
                .traceId(traceId)
                .raws(raws)
                .build();

        doNothing().when(alarmService).send(any(), any());

        // when
        boolean result = service.send(request);

        // then
        assertThat(result).isEqualTo(true);
        assertThat(memoryAppender.getSize()).isEqualTo(1);
        assertThat(memoryAppender.contains(String.format("%s: userId:%s traceId:%s massage:%s", "DistributionService", request.getUserId(), request.getTraceId(), "메세지 적재 완료"), Level.INFO)).isTrue();
    }
}