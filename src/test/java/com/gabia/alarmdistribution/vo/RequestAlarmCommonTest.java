package com.gabia.alarmdistribution.vo;

import com.gabia.alarmdistribution.vo.request.Raw;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestAlarmCommonTest {

    private ArrayList<Raw> raws;

    @Test
    public void lombok_테스트(){
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

        Raw raw1 = new Raw();
        raw1.setAppName(appName1);
        raw1.setAddress(address1);
        Raw raw2 = new Raw();
        raw2.setAppName(appName2);
        raw2.setAddress(address2);
        Raw raw3 = new Raw();
        raw3.setAppName(appName3);
        raw3.setAddress(address3);

        raws = new ArrayList<>(){
            {
                add(raw1);
                add(raw2);
                add(raw3);
            }
        };

        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";
        ArrayList<Integer> bookmarks = new ArrayList<>(){
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
