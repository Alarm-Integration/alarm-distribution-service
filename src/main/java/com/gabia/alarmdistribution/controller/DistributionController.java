package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.service.DistributionService;
import com.gabia.alarmdistribution.vo.ResponseObject;
import com.gabia.alarmdistribution.vo.request.RequestAlarmCommon;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistributionController {

    private final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping
    public ResponseEntity<ResponseObject> sendAlarm(@RequestBody RequestAlarmCommon request){
        ResponseObject response = new ResponseObject();
        response.setMessage("알림 전송 요청 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
