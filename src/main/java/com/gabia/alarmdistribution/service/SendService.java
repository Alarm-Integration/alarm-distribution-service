package com.gabia.alarmdistribution.service;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

public interface SendService {
    ListenableFuture<SendResult<String, Map<String, Object>>> send(Map<String, Object> data);
}
