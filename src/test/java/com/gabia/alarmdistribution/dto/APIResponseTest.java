package com.gabia.alarmdistribution.dto;

import com.gabia.alarmdistribution.dto.response.APIResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class APIResponseTest {
    @Test
    public void 정적_팩토리_메서드_테스트_성공() {
        //given
        String message = "response message";
        Object result = new Object();

        //when
        APIResponse apiResponse = APIResponse.withMessageAndResult(message, result);

        //then
        assertThat(apiResponse.getMessage()).isEqualTo(message);
        assertThat(apiResponse.getResult()).isEqualTo(result);
    }
}
