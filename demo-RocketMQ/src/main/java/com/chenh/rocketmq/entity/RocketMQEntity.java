package com.chenh.rocketmq.entity;

import lombok.Data;

@Data
public class RocketMQEntity {

    private String topic;

    private String messageBody;

    private String tag;

    private String keys;


}
