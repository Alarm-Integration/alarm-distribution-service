package com.gabia.alarmdistribution.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class AlarmMessage {
    private Long userId;
    private String traceId;
    private Long groupId;
    private List<String> addresses;
    private String title;
    private String content;

    @Builder
    public AlarmMessage(Long userId, String traceId, Long groupId, List<String> addresses, String title, String content) {
        this.userId = userId;
        this.traceId = traceId;
        this.groupId = groupId;
        this.addresses = addresses;
        this.title = title;
        this.content = content;
    }
}