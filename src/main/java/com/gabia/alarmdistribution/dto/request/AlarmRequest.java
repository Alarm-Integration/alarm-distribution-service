package com.gabia.alarmdistribution.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AlarmRequest {

    @NotNull
    private Long groupId;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Size(max=3)
    @NotEmpty
    private Map<String, List<String>> raws;

    @NotBlank
    private String traceId;

    @Builder
    public AlarmRequest(Long groupId, String title, String content, Map<String, List<String>> raws, Long userId, String traceId) {
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.raws = raws;
        this.userId = userId;
        this.traceId = traceId;
    }
}
