package com.gabia.alarmdistribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.RequestAlarmCommon;
import com.gabia.alarmdistribution.service.SendServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendServiceImpl service;

    @Test
    public void 사용자_알림_전송_컨트롤러_테스트() throws Exception {
        RequestAlarmCommon requestAlarmCommon = new RequestAlarmCommon();
        requestAlarmCommon.setGroupId(1L);
        requestAlarmCommon.setTitle("알림 제목");
        requestAlarmCommon.setContent("알림 내용");
        requestAlarmCommon.setBookmarks(Arrays.asList(1, 2));

        Raw slackRaw = Raw.builder()
                .appName("slack")
                .address(Arrays.asList("U1234", "U4321"))
                .build();

        Raw emailRaw = Raw.builder()
                .appName("email")
                .address(Arrays.asList("test@gmail.com", "test@naver.com"))
                .build();

        Raw smsRaw = Raw.builder()
                .appName("sms")
                .address(Arrays.asList("01012341234","01043214321"))
                .build();

        requestAlarmCommon.setRaws(Arrays.asList(slackRaw, emailRaw, smsRaw));


        given(service.send(requestAlarmCommon)).willReturn(true);

        this.mockMvc.perform(post("/")
                .content(asJsonString(requestAlarmCommon))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").exists());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
