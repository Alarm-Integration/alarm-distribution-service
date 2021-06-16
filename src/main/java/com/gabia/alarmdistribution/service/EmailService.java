package com.gabia.alarmdistribution.service;

import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService implements SendService{

    private Environment env;
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public EmailService(Environment env, KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean send(Map<String, Object> map) {
        String senderAddress = getSenderAddress(1L);
        map.put("sender", senderAddress);
        kafkaTemplate.send(env.getProperty("topic.email"), map);

        return true;
    }

    // 사용자 userId 값으로 보내는 주소(sender) 받아오기
    public String getSenderAddress(Long userId){
        return "gabia@gabia.com";
    }
}
