package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.dto.response.APIResponse;
import com.gabia.alarmdistribution.service.DistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "AlarmSendController")
@RestController
public class SendController {

    private DistributionService sendService;

    public SendController(DistributionService sendService) {
        this.sendService = sendService;
    }

    @ApiOperation(value = "알림 발송", notes = "3rd 파티 서비스로 알림 메세지를 발송합니다.")
    @PostMapping
    public ResponseEntity<APIResponse> sendAlarm(@ApiParam(value = "알림 요청", required = true) @RequestBody AlarmRequest alarmRequest) {
        boolean result = sendService.send(alarmRequest);

        return ResponseEntity.ok(APIResponse.withMessageAndResult("알림 전송 요청 완료", result));
    }
}
