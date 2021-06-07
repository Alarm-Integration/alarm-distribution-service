package com.gabia.alarmdistribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AlarmDistributionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlarmDistributionServiceApplication.class, args);
    }

}
