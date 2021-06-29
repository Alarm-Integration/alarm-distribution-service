package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.CommonAlarmRequest;
import com.gabia.alarmdistribution.dto.request.Raw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DistributionService {
    private final Map<String, SendService> sendService;

    public boolean send(CommonAlarmRequest request) {

        if (!checkUserInGroup(1L, request.getGroupId())) {
            log.error("DistributionService: 해당 그룹에 속한 유저가 아닙니다");
            return false;
        }

        //todo: CommonAlarmRequest.Raw.appname이 존재하는 서드 파티 앱인지 확인

        //todo: 북마크를 address로 변환 후 request의 raws로 넣어주기

        for (Raw raw : request.getRaws()) {
            String appName = raw.getAppName();

            if (!checkGroupAuthority(1L, appName)) {
                log.error("DistributionService: Group({})은 {} 발송 권한이 없습니다", request.getGroupId(), appName);
                return false;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("sender", getGroupSenderAddress(1L, appName));
            data.put("title", request.getTitle());
            data.put("content", request.getContent());
            data.put("raws", raw.getAddress());
            data.put("traceId", request.getTraceId());
            data.put("userId", request.getUserId());

            sendService.get(appName).send(data);
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

    private String getGroupSenderAddress(Long GroupId, String appName) {
        // todo: GroupId와 appName을 가지고 그룹의 대표 발신자 주소를 가져온다
        if (appName.equals("email"))
            return "nameks@naver.com";

        if (appName.equals("slack"))
            return "abc";

        if (appName.equals("sms"))
            return "01092988726";

        return "default";
    }

}