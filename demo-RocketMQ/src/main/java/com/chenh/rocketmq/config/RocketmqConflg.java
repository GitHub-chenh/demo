package com.chenh.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "rocketmq")
public class RocketmqConflg {
    private String producerGroup;

    private String nameServer;

    private String consumerGroup;
}
