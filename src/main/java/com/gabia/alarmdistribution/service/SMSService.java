package com.gabia.alarmdistribution.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service("sms")
public class SMSService implements SendService {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @Override
    public ListenableFuture<SendResult<String, Map<String, Object>>> send(Map<String, Object> data) {
        ListenableFuture<SendResult<String, Map<String, Object>>> future = kafkaTemplate.send("sms", data);

        future.addCallback(new ListenableFutureCallback<>() {
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
        log.info("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), userId, traceId, "메세지 적재 성공");
    }

    private void handleFailure(Map<String, Object> data, Throwable ex) {
        Long userId = (Long) data.get("userId");
        String traceId = (String) data.get("traceId");
        log.error("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), userId, traceId, ex.getMessage());
    }
}
