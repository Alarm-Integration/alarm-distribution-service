package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.vo.request.Raw;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SendServiceImpl {
    private final Map<String, SendService> sendService;

    public SendServiceImpl(EmailService emailService, SlackService slackService, SMSService smsService) {
        sendService = new HashMap<>();
        sendService.put("email", emailService);
        sendService.put("slack", slackService);
        sendService.put("sms", smsService);
    }

    public boolean send(RequestAlarmCommon alarmCommon) {
        // 분리 및 각 서비스로 전송
        for (Raw raw : alarmCommon.getRaws()) {
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", alarmCommon.getGroupId());
            map.put("title", alarmCommon.getTitle());
            map.put("content", alarmCommon.getTitle());
            map.put("bookmarks", alarmCommon.getBookmarks());
            map.put("raws", raw);
            // 추후 User Id 값 포함 넘기기
//            map.put("userId", userId);
            sendService.get(raw.getAppName()).send(map);
        }

        return true;
    }
}