package com.gabia.alarmdistribution.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AlarmRequest {

    @NotNull
    private Long groupId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotEmpty
    private Map<String, List<String>> receivers;

    @Builder
    public AlarmRequest(Long groupId, String title, String content, Map<String, List<String>> receivers) {
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.receivers = receivers;
    }
}
