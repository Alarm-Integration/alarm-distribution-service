package com.gabia.alarmdistribution.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.util.LogSender;
import com.gabia.alarmdistribution.util.MemoryAppender;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @Mock
    private LogSender logSender;

    @Mock
    private KafkaTemplate<String, AlarmMessage> kafkaTemplate;

    @InjectMocks
    private AlarmService alarmService;

    private MemoryAppender memoryAppender;

    //default
    private Long groupId = 1L;
    private Long userId = 1L;
    private List<String> addresses = Arrays.asList("test@gmail.com", "t123@gmail.com", "test@naver.com");
    private String title = "알림 제목";
    private String content = "알림 내용";
    private String traceId = "abc";
    private String topic = "email";

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
    void send_메서드_레코드_적재_성공() {
        // given
        String topic = "email";
        AlarmMessage message = createDefaultAlarmMessage();

        ProducerRecord<String, AlarmMessage> record = new ProducerRecord<>(topic, message);

        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(topic, 1),
                1,1,342, System.currentTimeMillis(), 1, 2);

        SendResult<String, AlarmMessage> sendResult= new SendResult<>(record, recordMetadata);

        SettableListenableFuture<SendResult<String, AlarmMessage>> future = new SettableListenableFuture<>();
        future.set(sendResult);

        given(kafkaTemplate.send(topic, message)).willReturn(future);

        // when
        alarmService.send(topic, message);

        //then
        assertThat(memoryAppender.getSize()).isEqualTo(1);
        assertThat(memoryAppender.contains(String.format("%s: userId:%s traceId:%s massage:%s", "AlarmService", userId, traceId, "메세지 적재 성공"), Level.INFO)).isTrue();
    }

    @Test
    void send_메서드_레코드_적재_실패() {
        // given
        AlarmMessage message = createDefaultAlarmMessage();

        SettableListenableFuture<SendResult<String, AlarmMessage>> future = new SettableListenableFuture<>();
        future.setException(new RuntimeException("Exception Calling Kafka"));

        given(kafkaTemplate.send("email", message)).willReturn(future);

        // when
        alarmService.send(topic, message);

        //then
        assertThat(memoryAppender.getSize()).isEqualTo(1);
        assertThat(memoryAppender.contains(String.format("%s: userId:%s traceId:%s massage:%s", "AlarmService", userId, traceId, "Exception Calling Kafka"), Level.ERROR)).isTrue();
    }

    private AlarmMessage createDefaultAlarmMessage() {
        return AlarmMessage.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .receivers(addresses)
                .title(title)
                .content(content)
                .build();
    }
}