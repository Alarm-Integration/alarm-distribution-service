package com.gabia.alarmdistribution.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonAlarmRequest {
    private Long groupId;
    private String title;
    private String content;
    private List<Integer> bookmarks;
    private List<Raw> raws;
    private Long userId;
    private String traceId;

    @Builder
    public CommonAlarmRequest(Long groupId, String title, String content, List<Integer> bookmarks, List<Raw> raws, Long userId, String traceId) {
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.bookmarks = bookmarks;
        this.raws = raws;
        this.userId = userId;
        this.traceId = traceId;
    }
}
