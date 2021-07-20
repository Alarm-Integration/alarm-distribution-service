package com.gabia.alarmdistribution;

import com.gabia.alarmdistribution.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(AppProperties.class)
@EnableDiscoveryClient
@SpringBootApplication
public class AlarmDistributionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlarmDistributionServiceApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
