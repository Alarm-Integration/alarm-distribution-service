package com.gabia.alarmdistribution.service;

import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SMSService implements SendService {

    private Environment env;
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public SMSService(Environment env, KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean send(Map<String, Object> map) {
        String senderNumber = getSenderNumber(1L);
        map.put("sender", senderNumber);
        kafkaTemplate.send(env.getProperty("topic.sms"), map);

        return true;
    }

    // 사용자 userId 값으로 보내는 번호(sender) 받아오기
    public String getSenderNumber(Long userId) {
        return "1544-4370";
    }
}
