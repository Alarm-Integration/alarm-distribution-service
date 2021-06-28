package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.CommonAlarmRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
    public void send_테스트() {
        // given
        String appName = "slack";

        ArrayList<String> address = new ArrayList<>() {
            {
                add("T13DA561");
                add("U13DA561");
                add("C13DA561");
            }
        };

        Raw raw = Raw.builder()
                .appName(appName)
                .address(address)
                .build();

        List<Raw> raws = Arrays.asList(raw);

        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";
        List<Integer> bookmarksIds = Arrays.asList(1);

        CommonAlarmRequest commonAlarmRequest = CommonAlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .bookmarks(bookmarksIds)
                .raws(raws)
                .build();

        emailService = mock(EmailService.class);
        slackService = mock(SlackService.class);
        smsService = mock(SMSService.class);
        sendService = new HashMap<>();
        sendService.put("email", emailService);
        sendService.put("slack", slackService);
        sendService.put("sms", smsService);

        // when
        given(service.send(commonAlarmRequest)).willReturn(true);

        // then
        assertThat(service.send(commonAlarmRequest)).isEqualTo(true);
    }
}