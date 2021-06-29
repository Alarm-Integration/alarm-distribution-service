package com.gabia.alarmdistribution.dto;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.CommonAlarmRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonAlarmRequestTest {

    @Test
    void builder_테스트() {
        //given
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
        List<Integer> bookmarksIds = Arrays.asList(1, 2, 3);
        Long userId = 1L;
        String traceId = "123";

        //when
        CommonAlarmRequest commonAlarmRequest = CommonAlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .bookmarks(bookmarksIds)
                .raws(raws)
                .userId(userId)
                .traceId(traceId)
                .build();

        //then
        assertThat(commonAlarmRequest.getGroupId()).isEqualTo(groupId);
        assertThat(commonAlarmRequest.getTitle()).isEqualTo(title);
        assertThat(commonAlarmRequest.getContent()).isEqualTo(content);
        assertThat(commonAlarmRequest.getBookmarks()).isEqualTo(bookmarksIds);
        assertThat(commonAlarmRequest.getRaws()).isEqualTo(raws);
        assertThat(commonAlarmRequest.getUserId()).isEqualTo(userId);
        assertThat(commonAlarmRequest.getTraceId()).isEqualTo(traceId);

    }
}
