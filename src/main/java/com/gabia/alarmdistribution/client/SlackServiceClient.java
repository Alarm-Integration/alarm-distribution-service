package com.gabia.alarmdistribution.client;

import com.gabia.alarmdistribution.vo.ResponseObject;
import com.gabia.alarmdistribution.vo.request.RequestSendAlarm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="slack-service")
public interface SlackServiceClient {
    @PostMapping("/slack-service/send-alarm")
    ResponseEntity<ResponseObject> sendAlarm(@RequestBody RequestSendAlarm request);
}
