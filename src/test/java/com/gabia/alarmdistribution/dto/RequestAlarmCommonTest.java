package com.gabia.alarmdistribution.dto;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestAlarmCommonTest {

    @Test
    public void lombok_테스트() {
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

        RequestAlarmCommon alarmCommon = new RequestAlarmCommon();
        alarmCommon.setGroupId(groupId);
        alarmCommon.setTitle(title);
        alarmCommon.setContent(content);
        alarmCommon.setBookmarks(bookmarksIds);
        alarmCommon.setRaws(raws);

        assertThat(alarmCommon.getGroupId()).isEqualTo(groupId);
        assertThat(alarmCommon.getTitle()).isEqualTo(title);
        assertThat(alarmCommon.getContent()).isEqualTo(content);
        assertThat(alarmCommon.getBookmarks()).isEqualTo(bookmarksIds);
        assertThat(alarmCommon.getRaws()).isEqualTo(raws);
    }
}
