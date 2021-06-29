package com.gabia.alarmdistribution.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;

@Slf4j
@Service("email")
public class EmailService implements SendService{

    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public EmailService(KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ListenableFuture<SendResult<String, Map<String, Object>>> send(Map<String, Object> data) {
        ListenableFuture<SendResult<String, Map<String, Object>>> future = kafkaTemplate.send("email", data);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Map<String, Object>>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(data, ex);
            }

            @Override
            public void onSuccess(SendResult<String, Map<String, Object>> result) {
                handleSuccess(data);
            }
        });
        return future;
    }

    private void handleSuccess(Map<String, Object> data) {
        String traceId = (String) data.get("traceId");
        Long userId = (Long) data.get("userId");
        log.info("EmailService: userId:{} traceId:{} 메세지 적재 성공", userId, traceId);
    }

    private void handleFailure(Map<String, Object> data, Throwable ex) {
        Long userId = (Long) data.get("userId");
        String traceId = (String) data.get("traceId");
        log.error("EmailService: userId:{} traceId:{} 메세지 적재 실패 {}", userId, traceId, ex.getMessage());
    }
}
