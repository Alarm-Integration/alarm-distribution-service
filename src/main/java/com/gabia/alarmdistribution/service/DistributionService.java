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

    public void send(AlarmRequest request) throws Exception {

        if (!checkUserInGroup(request.getUserId(), request.getGroupId())) {
            log.error("DistributionService: 사용자({})은 그룹({})에 속하지 않습니다", request.getUserId(), request.getGroupId());
            throw new Exception(String.format("DistributionService: 사용자(%s)가 그룹(%s)에 속하지 않습니다", request.getUserId(), request.getGroupId()));
        }

        for (Map.Entry<String, List<String>> entry : request.getRaws().entrySet()) {
            String appName = entry.getKey();
            List<String> receivers = entry.getValue();

            if (!checkGroupAuthority(request.getGroupId(), appName)) {
                log.error("DistributionService: Group({})은 {} 발송 권한이 없습니다", request.getGroupId(), appName);
                throw new Exception(String.format("DistributionService: Group(%s)은 %s 발송 권한이 없습니다", request.getGroupId(), appName));
            }

            AlarmMessage alarmMessage = AlarmMessage.builder()
                    .userId(request.getUserId())
                    .traceId(request.getTraceId())
                    .groupId(request.getGroupId())
                    .receivers(receivers)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();

            alarmService.send(appName, alarmMessage);
        }

        log.info("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), request.getUserId(), request.getTraceId(), "메세지 적재 완료");
    }

    private boolean checkUserInGroup(Long userId, Long GroupId) {
        // todo: 유저가 그룹에 속하는지 확인
        return true;
    }

    private boolean checkGroupAuthority(Long GroupId, String appName) {
        // todo: GroupId로 그룹을 조회후 해당 그룹이 서드파티(appName) 발송 권한을 가지고 있는지 확인
        return true;
    }
}


