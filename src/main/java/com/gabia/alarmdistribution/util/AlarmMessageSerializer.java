package com.gabia.alarmdistribution.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import org.apache.kafka.common.serialization.Serializer;

public class AlarmMessageSerializer implements Serializer<AlarmMessage> {

    @Override
    public byte[] serialize(String topic, AlarmMessage message) {
        byte[] data = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            data = objectMapper.writeValueAsString(message).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
