package com.gabia.alarmdistribution.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseObjectTest {
    @Test
    public void lombok_테스트(){
        String message = "response message";
        Object object = new Object();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setMessage(message);
        responseObject.setResult(object);

        assertThat(responseObject.getMessage()).isEqualTo(message);
        assertThat(responseObject.getResult()).isEqualTo(object);
    }
}
