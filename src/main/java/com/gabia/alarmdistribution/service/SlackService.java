package com.gabia.alarmdistribution.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SlackService implements SendService{

    @Override
    public String send(Map<String, Object> map) {
        String accessToken = getAccessToken(1L, 1L);

        

        return null;
    }


    // DB에서 엑세스 토큰 받아오기
    public String getAccessToken(Long groupId, Long userId){
        return "xoxb-2148325514801-2142207279172-ttsneJk3GUgXqkw3dtPPK5bS";
    }
}
