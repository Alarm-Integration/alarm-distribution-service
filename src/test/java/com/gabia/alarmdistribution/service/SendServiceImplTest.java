package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.vo.request.Raw;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SendServiceImplTest {

    @Mock
    private Map<String, SendService> sendService;

    @Mock
    EmailService emailService;

    @Mock
    SlackService slackService;

    @Mock
    SMSService smsService;

    @InjectMocks
    private SendServiceImpl service;

    @Test
    public void send_테스트(){
        // given
        String appName = "slack";

        ArrayList<String> address = new ArrayList<>() {
            {
                add("T13DA561");
                add("U13DA561");
                add("C13DA561");
            }
        };

        Raw raw = new Raw();
        raw.setAppName(appName);
        raw.setAddress(address);
        ArrayList<Raw> raws = new ArrayList<>(){
            {
                add(raw);
            }
        };


        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";
        ArrayList<Integer> bookmarks = new ArrayList<>(){
            {
                add(1);
            }
        };

        RequestAlarmCommon alarmCommon = new RequestAlarmCommon();
        alarmCommon.setGroupId(groupId);
        alarmCommon.setTitle(title);
        alarmCommon.setContent(content);
        alarmCommon.setBookmarks(bookmarks);
        alarmCommon.setRaws(raws);

        emailService = mock(EmailService.class);
        slackService = mock(SlackService.class);
        smsService = mock(SMSService.class);
        sendService = new HashMap<>();
        sendService.put("email", emailService);
        sendService.put("slack", slackService);
        sendService.put("sms", smsService);

        // when
        given(service.send(alarmCommon)).willReturn(true);

        // then
        assertThat(service.send(alarmCommon)).isEqualTo(true);
    }
}