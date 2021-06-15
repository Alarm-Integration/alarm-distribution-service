package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.vo.request.Raw;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @Mock
    private Environment env;

    @InjectMocks
    private EmailService emailService;

    @Test
    void getSenderAddressTest(){
        // Inmemory DB 삽입(userId, senderAddress);

        // DB 접속 및 senderAddress 가져오기
        // String senderAddress =  getSenderAddress(userId);
        String accessToken = emailService.getSenderAddress(1L);

        assertThat(accessToken).isEqualTo("");
    }

    @Test
    void sendTest(){
        // given
        String appName = "email";

        ArrayList<String> address = new ArrayList<>() {
            {
                add("test@gmail.com");
                add("t123@gmail.com");
                add("test@naver.com");
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

        Map<String, Object> map = new HashMap<>();
        map.put("groupId", alarmCommon.getGroupId());
        map.put("title", alarmCommon.getTitle());
        map.put("content", alarmCommon.getTitle());
        map.put("bookmarks", alarmCommon.getBookmarks());
        map.put("raws", raws);

        ListenableFuture listenableFuture = mock(ListenableFuture.class);
        given(env.getProperty("topic.email")).willReturn("email");
        given(kafkaTemplate.send(env.getProperty("topic.email"), map)).willReturn(listenableFuture);

        // when
        boolean isSend = emailService.send(map);

        // then
        assertThat(isSend).isEqualTo(true);
    }
}