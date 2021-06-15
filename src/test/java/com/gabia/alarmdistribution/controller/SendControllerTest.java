package com.gabia.alarmdistribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.alarmdistribution.service.SendServiceImpl;
import com.gabia.alarmdistribution.vo.request.Raw;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
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

    @Autowired
    private ObjectMapper mapper;

    @Mock
    private SendServiceImpl service;

    @Test
    public void 사용자_알림_전송_컨트롤러_테스트() throws Exception {
        RequestAlarmCommon requestAlarmCommon = new RequestAlarmCommon();
        requestAlarmCommon.setGroupId(1L);
        requestAlarmCommon.setTitle("알림 제목");
        requestAlarmCommon.setContent("알림 내용");
        requestAlarmCommon.setBookmarks(new ArrayList<>() {
            {
                add(1);
                add(2);
            }
        });
        requestAlarmCommon.setRaws(new ArrayList<>() {
            {
                Raw raw = new Raw();
                raw.setAppName("slack");
                raw.setAddress(new ArrayList<>() {
                    {
                        add("U1234");
                        add("U4321");
                    }
                });
                add(raw);

                raw.setAppName("email");
                raw.setAddress(new ArrayList<>() {
                    {
                        add("test@gmail.com");
                        add("test@naver.com");
                    }
                });
                add(raw);

                raw.setAppName("sms");
                raw.setAddress(new ArrayList<>() {
                    {
                        add("01012341234");
                        add("01043214321");
                    }
                });
                add(raw);
            }
        });

        String request = mapper.writeValueAsString(requestAlarmCommon);

        given(service.send(requestAlarmCommon)).willReturn(true);

        this.mockMvc.perform(post("/")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("알림 전송 요청 완료")))
                .andExpect(jsonPath("$.result", is(true)));
    }
}
