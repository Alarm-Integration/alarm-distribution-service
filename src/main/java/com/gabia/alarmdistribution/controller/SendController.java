package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.dto.response.APIResponse;
import com.gabia.alarmdistribution.service.DistributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    private DistributionService sendService;

    public SendController(DistributionService sendService) {
        this.sendService = sendService;
    }

    @PostMapping
    public ResponseEntity<APIResponse> sendAlarm(@RequestBody AlarmRequest request) {
        boolean result = sendService.send(request);

        return ResponseEntity.ok(APIResponse.withMessageAndResult("알림 전송 요청 완료", result));
    }
}
