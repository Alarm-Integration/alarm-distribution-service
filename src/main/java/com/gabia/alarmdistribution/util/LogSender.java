package com.gabia.alarmdistribution.util;

import com.gabia.alarmdistribution.dto.request.AlarmMessage;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import org.komamitsu.fluency.Fluency;
import org.komamitsu.fluency.fluentd.FluencyBuilderForFluentd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogSender {

    @Value("${fluentd.uri}")
    private String fluentdServerHost;

    @Value("${fluentd.port}")
    private int fluentdServerPort;

    public void sendAlarmRequest(Long userId, String traceId, AlarmRequest request) throws IOException {
        String tag = "alarm.request.access";

        Map<String, Object> event = new HashMap<>();
        event.put("user_id", userId);
        event.put("request_id", traceId);
        event.put("title", request.getTitle());
        event.put("content", request.getContent());
        event.put("created_at", LocalDateTime.now().toString());

        Fluency fluency = new FluencyBuilderForFluentd().build(fluentdServerHost, fluentdServerPort);
        fluency.emit(tag, event);
        fluency.close();
    }

    public void sendAlarmResults(String appName, String requestId, String address) throws IOException {
        String tag = "alarm.result.access";

        Map<String, Object> event = new HashMap<>();
        event.put("app_name", appName);
        event.put("request_id", requestId);
        event.put("log_message", "시스템 장애");
        event.put("is_success", false);
        event.put("address", address);

        Fluency fluency = new FluencyBuilderForFluentd().build(fluentdServerHost, fluentdServerPort);
        fluency.emit(tag, event);
        fluency.close();
    }
}
