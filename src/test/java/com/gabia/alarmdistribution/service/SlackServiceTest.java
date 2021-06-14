package com.gabia.alarmdistribution.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SlackServiceTest {
    @Autowired
    SlackService service;

    @Test
    public void getAccessTokenTest(){
        // Inmemory DB 삽입(groupId, userId, accessToken);

        // DB 접속 및 AccessToken 가져오기
        // String accessToken =  getAccessToken(groupId, userId);
        String accessToken = service.getAccessToken(1L, 1L);

        assertThat(accessToken).isEqualTo("");
    }
}
