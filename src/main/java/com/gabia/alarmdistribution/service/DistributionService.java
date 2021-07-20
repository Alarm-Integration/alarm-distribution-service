package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.util.LogSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistributionService {
    private final AlarmService alarmService;
    private final LogSender logSender;

    public void send(Long userId, String traceId, AlarmRequest request) {

        Map<String, List<String>> receivers = request.getReceivers();

        try {
            logSender.sendAlarmRequest(userId, traceId, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        receivers.forEach((appName, addresses) -> {
            AlarmMessage alarmMessage = AlarmMessage.builder()
                    .userId(userId)
                    .traceId(traceId)
                    .groupId(request.getGroupId())
                    .addresses(addresses)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();

            alarmService.send(appName, alarmMessage);
        });

        log.info("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), userId, traceId, "메세지 적재 완료");
    }
}


