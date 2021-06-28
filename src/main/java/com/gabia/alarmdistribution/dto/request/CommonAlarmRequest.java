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

    @Builder
    public CommonAlarmRequest(Long groupId, String title, String content, List<Integer> bookmarks, List<Raw> raws) {
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.bookmarks = bookmarks;
        this.raws = raws;
    }
}
