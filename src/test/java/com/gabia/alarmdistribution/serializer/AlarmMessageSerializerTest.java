package com.gabia.alarmdistribution.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.util.AlarmMessageSerializer;
import org.apache.kafka.common.serialization.Deserializer;
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

    class AlarmMessageDeserializer implements Deserializer<AlarmMessage> {

        @Override
        public AlarmMessage deserialize(String topic, byte[] data) {
            ObjectMapper mapper = new ObjectMapper();
            AlarmMessage alarmMessage = null;

            try {
                alarmMessage = mapper.readValue(data, AlarmMessage.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return alarmMessage;
        }
    }

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