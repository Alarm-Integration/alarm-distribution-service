package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistributionService {
    private final AlarmService alarmService;

    public void send(AlarmRequest request) {

        request.getRaws().forEach((appName, receivers) -> {
            AlarmMessage alarmMessage = AlarmMessage.builder()
                    .userId(request.getUserId())
                    .traceId(request.getTraceId())
                    .groupId(request.getGroupId())
                    .receivers(receivers)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();

            alarmService.send(appName, alarmMessage);
        });

        log.info("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), request.getUserId(), request.getTraceId(), "메세지 적재 완료");
    }
}