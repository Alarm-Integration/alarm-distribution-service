package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.service.SlackService;
import com.gabia.alarmdistribution.vo.ResponseObject;
import com.gabia.alarmdistribution.vo.request.RequestSendAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/alarm-distribution-service")
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/sendSlackAlarm")
    public ResponseEntity<ResponseObject> sendAlarm(){
        ResponseEntity<ResponseObject> response;
        ResponseObject result = new ResponseObject();
        try {
            slackService.sendSlackAlarm();
            result.setResult("test");
            result.setMessage("성공적으로 알람을 보냈습니다");
            response = ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.setResult(e.getMessage());
            result.setMessage("알람 전송 중 에러가 발생했습니다");
            response = ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return response;
    }

}
