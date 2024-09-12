package com.chenh.rocketmq.controller;

import com.chenh.rocketmq.entity.RocketMQEntity;
import com.chenh.rocketmq.service.ConsumerService;
import com.chenh.rocketmq.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * mq controller
 */
@RequestMapping("/mq")
@RestController
@RequiredArgsConstructor
public class RocketMQController {

    private final ProducerService producerService;

    private final ConsumerService consumerService;


    /**
     * 消息生产者
     * @return
     */
    @PostMapping(value = "/send",produces = "application/json;charset=utf-8")
    public boolean producer(
//            @RequestBody RocketMQEntity rocketMQEntity
    ){

        RocketMQEntity entity = new RocketMQEntity();
        entity.setTopic("test_topic");
        entity.setTag("test_tag");
        entity.setKeys("test_keys");
        entity.setMessageBody("我是一个测试消息文本");

        return producerService.getNormalMessageProvider(
                entity.getTopic(),
                entity.getMessageBody(),
                entity.getTag(),
                entity.getKeys());
    }

    /**
     * 消息消费者
     * @return
     */
    @PostMapping(value = "/receive",produces = "application/json;charset=utf-8")
    public boolean consumer(
//            @RequestBody RocketMQEntity rocketMQEntity
    ){

        RocketMQEntity entity = new RocketMQEntity();
        entity.setTopic("test_topic");
        entity.setTag("test_tag");
        entity.setKeys("test_keys");
        try {
            consumerService.getSimpleMessageConsumer(entity.getTopic(), entity.getTag(),entity.getKeys());
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
