package com.gabia.alarmdistribution.service;

import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("slack")
public class SlackService implements SendService{

    private Environment env;
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public SlackService(Environment env, KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean send(Map<String, Object> map) {
        String accessToken = getAccessToken(1L, 1L);
        map.put("accessToken", accessToken);

        kafkaTemplate.send(env.getProperty("topic.slack"), map);

        return true;
    }


    // DB에서 엑세스 토큰 받아오기
    public String getAccessToken(Long groupId, Long userId){
        return "xoxb-2148325514801-2142207279172-ttsneJk3GUgXqkw3dtPPK5bS";
    }
}
