package com.gabia.alarmdistribution.dto;

import com.gabia.alarmdistribution.dto.request.Raw;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RawTest {
    @Test
    public void builder_테스트() {
        //given
        String appName = "slack";
        List<String> address = Arrays.asList("T13DA561", "U13DA561", "C13DA561");

        //when
        Raw raw = Raw.builder()
                .appName(appName)
                .address(address)
                .build();

        //then
        assertThat(raw.getAppName()).isEqualTo(appName);
        assertThat(raw.getAddress()).isEqualTo(address);
    }
}
