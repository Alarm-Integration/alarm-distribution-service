package com.gabia.alarmdistribution.dto;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AlarmRequestTest {

    @Test
    void builder_테스트() {
        //given
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";

        //when
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        //then
        assertThat(alarmRequest.getGroupId()).isEqualTo(groupId);
        assertThat(alarmRequest.getTitle()).isEqualTo(title);
        assertThat(alarmRequest.getContent()).isEqualTo(content);
        assertThat(alarmRequest.getRaws()).isEqualTo(raws);
    }
}
