package com.gabia.alarmdistribution.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class AlarmMessageSerializer implements Serializer<Object> {

    @Override
    public byte[] serialize(String topic, Object message) {
        byte[] data;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            data = objectMapper.writeValueAsString(message).getBytes();
        } catch (Exception e) {
            log.error("{}: massage:{}", getClass().getSimpleName(), "Serializer error");
            throw new RuntimeException("Serializer error");
        }

        return data;
    }
}
