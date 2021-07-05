package com.gabia.alarmdistribution.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class AlarmMessageSerializer implements Serializer<Object> {

    @Override
    public byte[] serialize(String topic, Object message) {
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
