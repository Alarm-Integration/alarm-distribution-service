package com.gabia.alarmdistribution.service;

import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sms")
public class SMSService implements SendService {

    private Environment env;
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public SMSService(Environment env, KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean send(Map<String, Object> data) {
        kafkaTemplate.send(env.getProperty("topic.sms"), data);

        return true;
    }
}
