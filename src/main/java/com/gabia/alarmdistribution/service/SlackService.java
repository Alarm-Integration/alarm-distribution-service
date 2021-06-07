package com.gabia.alarmdistribution.service;

import com.gabia.alarmdistribution.client.SlackServiceClient;
import com.gabia.alarmdistribution.vo.request.RequestSendAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final SlackServiceClient slackServiceClient;

    public void sendSlackAlarm() {
        RequestSendAlarm requestSendAlarm = new RequestSendAlarm();
        requestSendAlarm.setId("C023WJKCPUM");
        requestSendAlarm.setText("this is test test test");
        slackServiceClient.sendAlarm(requestSendAlarm);
    }


}
