package com.gabia.alarmdistribution.dto.response;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Builder
@Getter
public class AlarmResponse {
    private String requestId;

    @NotNull
    private Long groupId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public static AlarmResponse createFrom(String requestId, AlarmRequest alarmRequest) {
        return AlarmResponse.builder()
                .requestId(requestId)
                .groupId(alarmRequest.getGroupId())
                .title(alarmRequest.getTitle())
                .content(alarmRequest.getContent())
                .build();
    }
}
