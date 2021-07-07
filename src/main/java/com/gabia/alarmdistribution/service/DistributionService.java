package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.dto.request.Raw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistributionService {
    private final AlarmService alarmService;

    public boolean send(AlarmRequest request) {

        if (!checkUserInGroup(1L, request.getGroupId())) {
            log.error("DistributionService: 해당 그룹에 속한 유저가 아닙니다");
            return false;
        }

        //todo: CommonAlarmRequest.Raw.appname이 존재하는 서드 파티 앱인지 확인

        //todo: 북마크를 address로 변환 후 request의 raws로 넣어주기

        for (Raw raw : request.getRaws()) {
            String appName = raw.getAppName();

            if (!checkGroupAuthority(request.getGroupId(), appName)) {
                log.error("DistributionService: Group({})은 {} 발송 권한이 없습니다", request.getGroupId(), appName);
                return false;
            }

            AlarmMessage alarmMessage = AlarmMessage.builder()
                    .userId(request.getUserId())
                    .traceId(request.getTraceId())
                    .groupId(request.getGroupId())
                    .raws(raw.getAddress())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();

            alarmService.send(appName, alarmMessage);
        }

        log.info("{}: userId:{} traceId:{} massage:{}", getClass().getSimpleName(), request.getUserId(), request.getTraceId(), "메세지 적재 완료");
        return true;
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