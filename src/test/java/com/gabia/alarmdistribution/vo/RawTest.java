package com.gabia.alarmdistribution.vo;

import com.gabia.alarmdistribution.vo.request.Raw;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class RawTest {
    @Test
    public void lombok_테스트() {
        String appName1 = "slack";
        String appName2 = "sms";
        String appName3 = "email";

        ArrayList<String> address1 = new ArrayList<>() {
            {
                add("T13DA561");
                add("U13DA561");
                add("C13DA561");
            }
        };
        ArrayList<String> address2 = new ArrayList<>() {
            {
                add("01012344321");
                add("01037826481");
                add("01027594837");
            }
        };
        ArrayList<String> address3 = new ArrayList<>() {
            {
                add("test@gmail.com");
                add("tes1@naver.com");
                add("tes3@gabia.com");
            }
        };

        Raw raw1 = new Raw();
        raw1.setAppName(appName1);
        raw1.setAddress(address1);
        Raw raw2 = new Raw();
        raw2.setAppName(appName2);
        raw2.setAddress(address2);
        Raw raw3 = new Raw();
        raw3.setAppName(appName3);
        raw3.setAddress(address3);

        assertThat(raw1.getAppName()).isEqualTo(appName1);
        assertThat(raw1.getAddress()).isEqualTo(address1);
        assertThat(raw2.getAppName()).isEqualTo(appName2);
        assertThat(raw2.getAddress()).isEqualTo(address2);
        assertThat(raw3.getAppName()).isEqualTo(appName3);
        assertThat(raw3.getAddress()).isEqualTo(address3);
    }
}
