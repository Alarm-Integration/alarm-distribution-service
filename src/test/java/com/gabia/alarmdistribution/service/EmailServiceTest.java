package com.gabia.alarmdistribution.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
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
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @InjectMocks
    private EmailService emailService;

    private MemoryAppender memoryAppender;

    //default
    private Long groupId = 1L;
    private Long userId = 1L;
    private String sender = "sender@email.com";
    private List<String> raws = Arrays.asList("test@gmail.com", "t123@gmail.com", "test@naver.com");
    private String title = "알림 제목";
    private String content = "알림 내용";
    private String traceId = "abc";

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
    void send_메서드_레코드_적재_성공() throws ExecutionException, InterruptedException {
        // given
        Map<String, Object> data = createDefaultData();

        ProducerRecord<String, Map<String, Object>> record = new ProducerRecord<>("email", data);

        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("email", 1),
                1,1,342, System.currentTimeMillis(), 1, 2);

        SendResult<String, Map<String, Object>> sendResult= new SendResult<>(record, recordMetadata);

        SettableListenableFuture future = new SettableListenableFuture<>();
        future.set(sendResult);

        given(kafkaTemplate.send("email", data)).willReturn(future);

        // when
        ListenableFuture<SendResult<String, Map<String, Object>>> listenableFuture = emailService.send(data);

        //then
        SendResult<String, Map<String, Object>> result = listenableFuture.get();
        assertThat(result.getProducerRecord().value().get("groupId")).isEqualTo(groupId);
        assertThat(result.getProducerRecord().value().get("userId")).isEqualTo(userId);
        assertThat(result.getProducerRecord().value().get("sender")).isEqualTo(sender);
        assertThat(result.getProducerRecord().value().get("raws")).isEqualTo(raws);
        assertThat(result.getProducerRecord().value().get("title")).isEqualTo(title);
        assertThat(result.getProducerRecord().value().get("content")).isEqualTo(content);
        assertThat(result.getProducerRecord().value().get("traceId")).isEqualTo(traceId);
        assertThat(memoryAppender.getSize()).isEqualTo(1);
        assertThat(memoryAppender.contains(String.format("EmailService: userId:%s traceId:%s 메세지 적재 성공", userId, traceId), Level.INFO)).isTrue();
    }

    @Test
    void send_메서드_레코드_적재_실패() {
        // given
        Map<String, Object> data = createDefaultData();

        SettableListenableFuture future = new SettableListenableFuture<>();
        future.setException(new RuntimeException("Exception Calling Kafka"));

        given(kafkaTemplate.send("email", data)).willReturn(future);

        // when
        emailService.send(data);

        //then
        assertThat(memoryAppender.getSize()).isEqualTo(1);
        assertThat(memoryAppender.contains(String.format("EmailService: userId:%s traceId:%s 메세지 적재 실패 %s", userId, traceId, "Exception Calling Kafka"), Level.ERROR)).isTrue();
    }

    private Map<String, Object> createDefaultData() {
        Map<String, Object> data = new HashMap<>();
        data.put("groupId", groupId);
        data.put("userId", userId);
        data.put("sender", sender);
        data.put("raws", raws);
        data.put("title", title);
        data.put("content", content);
        data.put("traceId", traceId);
        return data;
    }
}