package com.gabia.alarmdistribution.vo.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class RequestAlarmCommon {
    private Long groupId;
    private String title;
    private String content;

    private ArrayList<Integer> bookmarks;

    private ArrayList<Raw> raws;
}
