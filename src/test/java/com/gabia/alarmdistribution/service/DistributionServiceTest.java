package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.CommonAlarmRequest;
import com.gabia.alarmdistribution.dto.request.Raw;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class DistributionServiceTest {

    @MockBean
    private EmailService emailService;

    @MockBean
    private SlackService slackService;

    @MockBean
    private SMSService smsService;

    @Autowired
    private DistributionService service;

    @Test
    public void send_테스트() {
        // given
        List<String> slackAddress = Arrays.asList("T13DA561", "U13DA561", "C13DA561");
        List<String> smsAddress = Arrays.asList("01012344321", "01037826481", "01027594837");
        List<String> emailAddress = Arrays.asList("test@gmail.com", "tes1@naver.com", "tes1@naver.com");

        Raw slackRaw = Raw.createSlackRaw(slackAddress);
        Raw emailRaw = Raw.createEmailRaw(emailAddress);
        Raw smsRaw = Raw.createSMSRaw(smsAddress);

        List<Raw> raws = Arrays.asList(slackRaw, smsRaw, emailRaw);

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

        given(emailService.send(any())).willReturn(true);
        given(slackService.send(any())).willReturn(true);
        given(smsService.send(any())).willReturn(true);

        // when
        boolean result = service.send(commonAlarmRequest);

        // then
        assertThat(result).isEqualTo(true);
    }
}