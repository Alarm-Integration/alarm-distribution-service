package com.gabia.alarmdistribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.dto.request.Raw;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.service.DistributionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

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
    private DistributionService service;

    @Test
    public void 사용자_알림_전송_컨트롤러_테스트() throws Exception {
        //given
        Long groupId = 1L;
        String title = "알림 제목";
        String content = "알림 내용";

        List<Integer> bookmarksIds = Arrays.asList(1, 2);

        Raw slackRaw = Raw.createSlackRaw(Arrays.asList("U1234", "U4321"));
        Raw smsRaw = Raw.createSMSRaw(Arrays.asList("01012341234", "01043214321"));
        Raw emailRaw = Raw.createEmailRaw(Arrays.asList("test@gmail.com", "test@naver.com"));

        List<Raw> raws = Arrays.asList(slackRaw, emailRaw, smsRaw);

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .bookmarks(bookmarksIds)
                .raws(raws)
                .build();

        given(service.send(alarmRequest)).willReturn(true);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").exists());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
