package com.gabia.alarmdistribution.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class AlarmMessageDeserializer implements Deserializer<AlarmMessage> {

    @Override
    public AlarmMessage deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        AlarmMessage alarmMessage;

        try {
            alarmMessage = mapper.readValue(data, AlarmMessage.class);
        } catch (Exception e) {
            log.error("{}: massage:{}", getClass().getSimpleName(), "Deserializer error");
            throw new RuntimeException("Deserializer error");
        }

        return alarmMessage;
    }
}
