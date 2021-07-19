package com.gabia.alarmdistribution.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class APIResponse {
    private String message;
    private Object result;

    public static APIResponse withMessageAndResult(String message, Object result) {
        return APIResponse.builder()
                .message(message)
                .result(result)
                .build();
    }

    @Builder
    public APIResponse(String message, Object result) {
        this.message = message;
        this.result = result;
    }
}
