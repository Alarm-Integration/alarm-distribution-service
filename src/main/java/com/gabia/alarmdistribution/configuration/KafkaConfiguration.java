package com.gabia.alarmdistribution.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value("${kafka.bootstrap.servers}")
    private String kafkaServer;

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

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return config;
    }
}
