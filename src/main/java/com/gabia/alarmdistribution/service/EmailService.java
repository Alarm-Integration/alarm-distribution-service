package com.gabia.alarmdistribution.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService implements SendService{
    @Override
    public String send(Map<String, Object> map) {
        return null;
    }
}
