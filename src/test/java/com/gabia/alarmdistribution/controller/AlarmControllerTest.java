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

    private Long userId = 1L;
    private String traceId = "abc";
    private Long groupId = 1L;
    private String title = "알림 제목";
    private String content = "알림 내용";

    @Test
    public void 사용자_알림_전송_성공() throws Exception {
        //given
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("알림 전송 요청 완료"))
                .andExpect(jsonPath("$.result.requestId").value(traceId))
                .andExpect(jsonPath("$.result.groupId").value(groupId))
                .andExpect(jsonPath("$.result.title").value(title))
                .andExpect(jsonPath("$.result.content").value(content));
    }

    @Test
    public void 사용자_알림_전송_실패_user_id_header_없음() throws Exception {
        //given
        String missingHeaderName = "user-id";
        String expectedErrorMessage = String.format("Required request header '%s' for method parameter type Long is not present", missingHeaderName);

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
//                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("MissingRequestHeaderException"))
                .andExpect(jsonPath("$.result.error_message").value(expectedErrorMessage))
                .andExpect(jsonPath("$.result.required_header_name").value(missingHeaderName));
    }

    @Test
    public void 사용자_알림_전송_실패_trace_id_header_없음() throws Exception {
        //given
        String missingHeaderName = "trace-id";
        String expectedErrorMessage = String.format("Required request header '%s' for method parameter type String is not present", missingHeaderName);

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
//                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("MissingRequestHeaderException"))
                .andExpect(jsonPath("$.result.error_message").value(expectedErrorMessage))
                .andExpect(jsonPath("$.result.required_header_name").value(missingHeaderName));
    }

    @Test
    public void 사용자_알림_전송_실패_groupId_없음() throws Exception {
        //given
        AlarmRequest alarmRequest = AlarmRequest.builder()
//                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
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
    public void 사용자_알림_전송_실패_title_없음() throws Exception {
        //given
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
//                .title(title)
                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
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
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
//                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
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
    public void 사용자_알림_전송_실패_receivers_없음() throws Exception {
        //given
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
//                .receivers(getDefaultRaw())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("receivers"))
                .andExpect(jsonPath("$.result.errors[0].code").value("NotEmpty"));
    }

    @Test
    public void 사용자_알림_전송_실패_groupId_title_content_없음() throws Exception {
        //given
        AlarmRequest alarmRequest = AlarmRequest.builder()
//                .groupId(groupId)
//                .title(title)
//                .content(content)
                .receivers(getDefaultReceivers())
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result..field").value(containsInAnyOrder("groupId", "title", "content")))
                .andExpect(jsonPath("$.result..code").value(containsInAnyOrder("NotNull", "NotBlank", "NotBlank")));
    }

    @Test
    public void 사용자_알림_전송_실패_email_형식_아님() throws Exception {
        //given
        Map<String, List<String>> receivers = new HashMap<>();
        receivers.put("slack", Arrays.asList("U1234", "U4321"));
        receivers.put("email", Arrays.asList("testnaver.com"));
        receivers.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(receivers)
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("receivers"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Type.email"))
                .andExpect(jsonPath("$.result.errors[0].message").value("email:메일 형식만 지원합니다"));
    }

    @Test
    public void 사용자_알림_전송_실패_전화번호_형식_아님() throws Exception {
        //given
        Map<String, List<String>> receivers = new HashMap<>();
        receivers.put("slack", Arrays.asList("U1234", "U4321"));
        receivers.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        receivers.put("sms", Arrays.asList("000A00000"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(receivers)
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("receivers"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Type.sms"))
                .andExpect(jsonPath("$.result.errors[0].message").value("sms:전화번호 형식만 지원합니다"));
    }

    @Test
    public void 사용자_알림_전송_실패_슬랙_형식_아님() throws Exception {
        //given
        Map<String, List<String>> receivers = new HashMap<>();
        receivers.put("slack", Arrays.asList("012900201"));
        receivers.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        receivers.put("sms", Arrays.asList("01012341234", "01043214321"));

        AlarmRequest alarmRequest = AlarmRequest.builder()
                .groupId(groupId)
                .title(title)
                .content(content)
                .receivers(receivers)
                .build();

        doNothing().when(service).send(userId, traceId, alarmRequest);

        //when
        ResultActions result = this.mockMvc.perform(post("/")
                .header("user-id", userId)
                .header("trace-id", traceId)
                .content(asJsonString(alarmRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("BindException"))
                .andExpect(jsonPath("$.result.errors[0].field").value("receivers"))
                .andExpect(jsonPath("$.result.errors[0].code").value("Type.slack"))
                .andExpect(jsonPath("$.result.errors[0].message").value("slack:유효한 아이디 값이 아닙니다"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> getDefaultReceivers() {
        Map<String, List<String>> receivers = new HashMap<>();
        receivers.put("slack", Arrays.asList("U1234", "U4321"));
        receivers.put("email", Arrays.asList("test@gmail.com", "test@naver.com"));
        receivers.put("sms", Arrays.asList("01012341234", "01043214321"));
        return receivers;
    }
}
