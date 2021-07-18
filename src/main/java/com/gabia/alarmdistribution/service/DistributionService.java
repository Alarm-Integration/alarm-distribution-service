package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistributionService {
    private final AlarmService alarmService;

    public void send(Long userId, String traceId, AlarmRequest request) {

        Map<String, List<String>> raws = request.getRaws();

        raws.forEach((appName, receivers) -> {
            AlarmMessage alarmMessage = AlarmMessage.builder()
                    .userId(userId)
                    .traceId(traceId)
                    .groupId(request.getGroupId())
                    .receivers(receivers)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();

            alarmService.send(appName, alarmMessage);
        });

        log.info("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), userId, traceId, "메세지 적재 완료");
    }
}


