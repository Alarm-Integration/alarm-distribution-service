package com.gabia.alarmdistribution.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
// 추후 Spring config server 에서 환경변수 값을 들고올 것
@PropertySource("classpath:kafka.properties")
public class KafkaConfiguration {
    private final Environment env;

    public KafkaConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public KafkaTemplate<String, Map<String, Object>> objectKafkaTemplate(){
        return new KafkaTemplate<>(objectProducerFactory());
    }

    private ProducerFactory<String, Map<String, Object>> objectProducerFactory(){
        return new DefaultKafkaProducerFactory<>(setConfig());
    }

    // 공통 설정, 전달 받은 객체 -> Json 화
    private Map<String, Object> setConfig(){
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("bootstrap.servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return config;
    }
}
