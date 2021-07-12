package com.gabia.alarmdistribution.serializer;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class AlarmMessageSerializerTest {
    private Long groupId = 1L;
    private Long userId = 1L;
    private String title = "title";
    private String content = "content";
    private String traceId = "abc";
    private List<String> addresses = Arrays.asList("receiver@email.com");

    private AlarmMessageSerializer alarmMessageSerializer = new AlarmMessageSerializer();
    private AlarmMessageDeserializer alarmMessageDeserializer = new AlarmMessageDeserializer();

    @Test
    void serialize_성공() {
        //given
        AlarmMessage message1 = AlarmMessage.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .receivers(addresses)
                .title(title)
                .content(content)
                .build();

        //when
        byte[] serialize = alarmMessageSerializer.serialize("topic", message1);
        AlarmMessage message2 = alarmMessageDeserializer.deserialize("topic", serialize);

        //then
        Assertions.assertThat(message1).isEqualTo(message2);
    }

}