package com.gabia.alarmdistribution.vo.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Raw {
    private String appName;
    private ArrayList<String> address;
}
