package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;

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
    void getSenderAddressTest() {
        // Inmemory DB 삽입(userId, senderAddress);

        // DB 접속 및 senderAddress 가져오기
        // String senderAddress =  getSenderAddress(userId);
        String accessToken = emailService.getSenderAddress(1L);

//        assertThat(accessToken).isEqualTo("gabia@gabia.com");
    }

    @Test
    void sendTest() {
        // given
        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";

        String appName = "email";
        List<String> address = Arrays.asList("test@gmail.com", "t123@gmail.com", "test@naver.com");
        Raw raw = Raw.builder()
                .appName(appName)
                .address(address)
                .build();
        List<Raw> raws = Arrays.asList(raw);

        List<Integer> bookmarks = Arrays.asList(1);

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