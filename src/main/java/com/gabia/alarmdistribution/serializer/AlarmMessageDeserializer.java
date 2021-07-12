package com.gabia.alarmdistribution.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import org.apache.kafka.common.serialization.Deserializer;

public class AlarmMessageDeserializer implements Deserializer<AlarmMessage> {

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
