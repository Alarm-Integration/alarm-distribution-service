package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.CommonAlarmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DistributionService {
    private final Map<String, SendService> sendService;

    public boolean send(CommonAlarmRequest alarmCommon) {
        // 분리 및 각 서비스로 전송
        for (Raw raw : alarmCommon.getRaws()) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", alarmCommon.getTitle());
            map.put("content", alarmCommon.getContent());
            map.put("raws", raw.getAddress());
            // todo
            // 그룹아이디로 권한 체크
            // 북마크 -> raws로 풀어서 리퀘스트 넘겨주기
            // 추후 User Id 값 포함 넘기기
            // map.put("userId", userId);
            sendService.get(raw.getAppName()).send(map);
        }

        return true;
    }
}