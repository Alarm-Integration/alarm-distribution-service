package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.CommonAlarmRequest;
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
class SMSServiceTest {

    @Mock
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @Mock
    private Environment env;

    @InjectMocks
    private SMSService smsService;

    @Test
    void getSenderNumberTest() {
        // Inmemory DB 삽입(userId, number);

        // DB 접속 및 senderNumber 가져오기
        // String senderNumber =  getSenderNumber(userId);
        String senderNumber = smsService.getSenderNumber(1L);

//        assertThat(senderNumber).isEqualTo("1544-4370");
    }

    @Test
    void sendTest() {
        // given
        String appName = "sms";

        ArrayList<String> address = new ArrayList<>() {
            {
                add("01012341234");
                add("01043214321");
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

        Map<String, Object> map = new HashMap<>();
        map.put("groupId", commonAlarmRequest.getGroupId());
        map.put("title", commonAlarmRequest.getTitle());
        map.put("content", commonAlarmRequest.getTitle());
        map.put("bookmarks", commonAlarmRequest.getBookmarks());
        map.put("raws", raws);

        ListenableFuture listenableFuture = mock(ListenableFuture.class);
        given(env.getProperty("topic.sms")).willReturn("sms");
        given(kafkaTemplate.send(env.getProperty("topic.sms"), map)).willReturn(listenableFuture);

        // when
        boolean isSend = smsService.send(map);

        // then
        assertThat(isSend).isEqualTo(true);
    }
}