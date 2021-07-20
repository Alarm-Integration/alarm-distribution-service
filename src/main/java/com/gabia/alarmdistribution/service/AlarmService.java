package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.util.LogSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {

    private final KafkaTemplate<String, AlarmMessage> kafkaTemplate;
    private final LogSender logSender;

    public void send(String topic, AlarmMessage alarmMessage) {
        ListenableFuture<SendResult<String, AlarmMessage>> future = kafkaTemplate.send(topic, alarmMessage);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(Throwable ex) {

                handleFailure(alarmMessage, topic, ex);
            }

            @Override
            public void onSuccess(SendResult<String, AlarmMessage> result) {
                handleSuccess(alarmMessage);
            }

        });
    }

    private void handleSuccess(AlarmMessage message) {
        log.info("{}: userId:{} traceId:{} massage:{}",
                getClass().getSimpleName(), message.getUserId(), message.getTraceId(), "메세지 적재 성공");
    }

    private void handleFailure(AlarmMessage message, String appName, Throwable ex) {
        log.error("{}: userId:{} traceId:{} massage:{}",
                getClass().getSimpleName(), message.getUserId(), message.getTraceId(), ex.getMessage());

        message.getAddresses().forEach(address -> {
            try {
                logSender.sendAlarmResults(appName, message.getTraceId(), address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}