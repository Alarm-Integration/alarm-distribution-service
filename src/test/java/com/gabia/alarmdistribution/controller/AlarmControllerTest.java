package com.gabia.alarmdistribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlarmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistributionService service;

    @Test
    public void 사용자_알림_전송_성공() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("알림 전송 요청 완료"))
                .andExpect(jsonPath("$.result").isEmpty());

    }

    @Test
    public void 사용자_알림_전송_실패_userId_없음() throws Exception {
        //given
//        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";

        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
//                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("userId"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotNull"));

    }


    @Test
    public void 사용자_알림_전송_실패_groupId_없음() throws Exception {
        //given
        Long userId = 1L;
//        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
//                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();


        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("groupId"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotNull"));

    }

    @Test
    public void 사용자_알림_전송_실패_traceId_없음() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
//        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
//                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("traceId"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotBlank"));

    }

    @Test
    public void 사용자_알림_전송_실패_title_없음() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
//        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
//                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("title"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotBlank"));

    }

    @Test
    public void 사용자_알림_전송_실패_content_없음() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
//        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
//                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("content"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotBlank"));
    }

    @Test
    public void 사용자_알림_전송_실패_raws_없음() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
//        Map<String, List<String>> raws = new HashMap<>();
//        raws.put("slack", Arrays.asList("U1234", "U4321"));
//        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
//        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
//                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("raws"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotEmpty"));
    }

    @Test
    public void 사용자_알림_전송_실패_userId_groupId_traceId_없음() throws Exception {
        //given
//        Long userId = 1L;
//        Long groupId = 1L;
//        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
//                .userId(userId)
//                .groupId(groupId)
//                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result..field").value(containsInAnyOrder("traceId", "groupId", "userId")))
                .andExpect(jsonPath("$.result..code").value(containsInAnyOrder("NotNull", "NotNull", "NotBlank")));
    }

    @Test
    public void 사용자_알림_전송_실패_email_형식_아님() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("testnaver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("raws"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Type"))
                .andExpect(jsonPath("$.result.errors[0].message").value("email 서비스는 email 형식만 지원합니다"));

    }

    @Test
    public void 사용자_알림_전송_실패_전화번호_형식_아님() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("000A00000"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("raws"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Type"))
                .andExpect(jsonPath("$.result.errors[0].message").value("sms 서비스는 전화번호 형식만 지원합니다"));
    }

    @Test
    public void 사용자_알림_전송_실패_슬랙_형식_아님() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("012900201"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("raws"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Type.slack"))
                .andExpect(jsonPath("$.result.errors[0].message").value("유효한 아이디 값이 아닙니다"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void 사용자_알림_전송_실패_4개_이상의_서비스_요청() throws Exception {
        //given
        Long userId = 1L;
        Long groupId = 1L;
        String traceId = "abc";
        String title = "알림 제목";
        String content = "알림 내용";
        Map<String, List<String>> raws = new HashMap<>();
        raws.put("slack", Arrays.asList("U1234", "U4321"));
        raws.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        raws.put("sms", Arrays.asList("01012341234", "01043214321"));
        raws.put("service", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .userId(userId)
                .groupId(groupId)
                .traceId(traceId)
                .title(title)
                .content(content)
                .raws(raws)
                .build();

        doNothing().when(service).send(alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("raws"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Size"));

    }

}
