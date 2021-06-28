package com.gabia.alarmdistribution.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class APIResponse {
    private String message;
    private Object result;

    public static APIResponse withMessage(String message) {
        return APIResponse.builder()
                .message(message)
                .result(null)
                .build();
    }

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
