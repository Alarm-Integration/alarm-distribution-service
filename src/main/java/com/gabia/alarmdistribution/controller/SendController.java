package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.service.SendServiceImpl;
import com.gabia.alarmdistribution.vo.ResponseObject;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    private SendServiceImpl sendService;

    public SendController(SendServiceImpl sendService) {
        this.sendService = sendService;
    }

    @PostMapping
    public ResponseEntity<ResponseObject> sendAlarm(@RequestBody RequestAlarmCommon request){
        ResponseObject response = new ResponseObject();
        response.setMessage("알림 전송 요청 완료");
        response.setResult(sendService.send(request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
