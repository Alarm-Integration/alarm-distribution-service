package com.gabia.alarmdistribution.dto;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestAlarmCommonTest {

    private ArrayList<Raw> raws;

    @Test
    public void lombok_테스트() {
        String appName1 = "slack";
        String appName2 = "sms";
        String appName3 = "email";

        ArrayList<String> address1 = new ArrayList<>() {
            {
                add("T13DA561");
                add("U13DA561");
                add("C13DA561");
            }
        };
        ArrayList<String> address2 = new ArrayList<>() {
            {
                add("01012344321");
                add("01037826481");
                add("01027594837");
            }
        };
        ArrayList<String> address3 = new ArrayList<>() {
            {
                add("test@gmail.com");
                add("tes1@naver.com");
                add("tes3@gabia.com");
            }
        };

        Raw raw1 = Raw.builder()
                .appName(appName1)
                .address(address1)
                .build();

        Raw raw2 = Raw.builder()
                .appName(appName2)
                .address(address2)
                .build();

        Raw raw3 = Raw.builder()
                .appName(appName3)
                .address(address3)
                .build();

        raws = new ArrayList<>() {
            {
                add(raw1);
                add(raw2);
                add(raw3);
            }
        };

        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";
        ArrayList<Integer> bookmarks = new ArrayList<>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };

        RequestAlarmCommon alarmCommon = new RequestAlarmCommon();
        alarmCommon.setGroupId(groupId);
        alarmCommon.setTitle(title);
        alarmCommon.setContent(content);
        alarmCommon.setBookmarks(bookmarks);
        alarmCommon.setRaws(raws);


        assertThat(alarmCommon.getGroupId()).isEqualTo(groupId);
        assertThat(alarmCommon.getTitle()).isEqualTo(title);
        assertThat(alarmCommon.getContent()).isEqualTo(content);
        assertThat(alarmCommon.getBookmarks()).isEqualTo(bookmarks);
        assertThat(alarmCommon.getRaws()).isEqualTo(raws);
    }
}
