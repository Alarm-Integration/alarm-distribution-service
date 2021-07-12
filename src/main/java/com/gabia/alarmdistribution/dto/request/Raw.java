package com.gabia.alarmdistribution.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Raw {
    private String appName;
    private List<String> address;

    @Builder
    public Raw(String appName, List<String> address) {
        this.appName = appName;
        this.address = address;
    }

    public static Raw createRaw(String appName, List<String> address) {
        return Raw.builder()
                .appName(appName)
                .address(address)
                .build();
    }
}
