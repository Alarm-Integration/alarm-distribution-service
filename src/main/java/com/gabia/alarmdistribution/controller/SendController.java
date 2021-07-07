package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.dto.response.APIResponse;
import com.gabia.alarmdistribution.service.DistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SendController {

    private final DistributionService sendService;

    @PostMapping
    public ResponseEntity<APIResponse> sendAlarm(@RequestBody AlarmRequest request) {
        sendService.send(request);
        return ResponseEntity.ok(APIResponse.withMessageAndResult("알림 전송 요청 완료", null));
    }
}
