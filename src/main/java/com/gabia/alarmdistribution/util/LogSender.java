package com.gabia.alarmdistribution.util;

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

    public void send(Long userId, String appName, String traceId, String resultMsg) throws IOException {
        String tag = "alarm.access";

        Map<String, Object> event = new HashMap<>();
        event.put("user_id", userId);
        event.put("app_name", appName);
        event.put("trace_id", traceId);
        event.put("result_msg", resultMsg);
        event.put("created_at", LocalDateTime.now().toString());
        Fluency fluency = new FluencyBuilderForFluentd().build(fluentdServerHost, fluentdServerPort);
        fluency.emit(tag, event);
        fluency.close();
    }
}
