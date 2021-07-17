package com.gabia.alarmdistribution.controller;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import com.gabia.alarmdistribution.dto.response.APIResponse;
import com.gabia.alarmdistribution.service.DistributionService;
import com.gabia.alarmdistribution.validator.AlarmRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final DistributionService sendService;
    private final AlarmRequestValidator alarmRequestValidator;

    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(alarmRequestValidator);
    }

    @PostMapping
    public ResponseEntity<APIResponse> sendAlarm(@RequestHeader(value = "user-id") Long userId,
                                                 @RequestHeader(value = "trace-id") String traceId,
                                                 @Valid @RequestBody AlarmRequest request,
                                                 BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        sendService.send(userId, traceId, request);
        return ResponseEntity.ok(APIResponse.withMessageAndResult("알림 전송 요청 완료", null));
    }
}
