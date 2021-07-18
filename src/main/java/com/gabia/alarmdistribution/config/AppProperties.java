package com.gabia.alarmdistribution.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "alarm")
public class AppProperties {

    private HashMap<String, Application> applications;

    @Data
    public static class Application {
        private String regex;
    }
}
