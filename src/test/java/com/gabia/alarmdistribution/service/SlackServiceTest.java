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
public class SlackServiceTest {

    @Mock
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @Mock
    private Environment env;

    @InjectMocks
    private SlackService slackService;

    @Test
    void getAccessTokenTest() {
        // Inmemory DB 삽입(groupId, userId, accessToken);

        // DB 접속 및 AccessToken 가져오기
        // String accessToken =  getAccessToken(groupId, userId);
        String accessToken = slackService.getAccessToken(1L, 1L);

        assertThat(accessToken).isEqualTo("xoxb-2148325514801-2142207279172-ttsneJk3GUgXqkw3dtPPK5bS");
    }

    @Test
    void sendTest() {
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

        Map<String, Object> map = new HashMap<>();
        map.put("groupId", commonAlarmRequest.getGroupId());
        map.put("title", commonAlarmRequest.getTitle());
        map.put("content", commonAlarmRequest.getTitle());
        map.put("bookmarks", commonAlarmRequest.getBookmarks());
        map.put("raws", raws);

        ListenableFuture listenableFuture = mock(ListenableFuture.class);
        given(env.getProperty("topic.slack")).willReturn("slack");
        given(kafkaTemplate.send(env.getProperty("topic.slack"), map)).willReturn(listenableFuture);

        // when
        boolean isSend = slackService.send(map);

        // then
        assertThat(isSend).isEqualTo(true);
    }
}
