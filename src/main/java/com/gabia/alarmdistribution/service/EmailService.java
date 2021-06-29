package com.gabia.alarmdistribution.service;

import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("email")
public class EmailService implements SendService{

    private Environment env;
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public EmailService(Environment env, KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean send(Map<String, Object> data) {
        String senderAddress = getSenderAddress(1L);
        data.put("sender", senderAddress);
        kafkaTemplate.send(env.getProperty("topic.email"), data);

        return true;
    }

    // 사용자 userId 값으로 보내는 주소(sender) 받아오기
    public String getSenderAddress(Long userId){
        return "nameks@naver.com";
    }
}
