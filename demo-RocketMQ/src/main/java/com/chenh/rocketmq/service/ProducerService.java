package com.chenh.rocketmq.service;

import com.chenh.rocketmq.config.RocketmqConflg;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.java.example.ProducerSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 消息生产者
 */
@Service
@RequiredArgsConstructor
public class ProducerService {
    private static final Logger log = LoggerFactory.getLogger(ProducerService.class);

    private final ClientServiceProvider provider = ClientServiceProvider.loadService();

    private final RocketmqConflg conflg;

    /**
     * 获取消费生产者（正常）
     */
    public void getNormalMessageProvider(String topic,String messageStr,String tag){
//        String topic = conflg.getTopic();
        final Producer producer;
        try {
            producer = ProducerSingleton.getInstance(topic);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        // Define your message body.
//        byte[] body = "This is a normal message for Apache RocketMQ".getBytes(StandardCharsets.UTF_8);
        byte[] body = messageStr.getBytes(StandardCharsets.UTF_8);
//        String tag = "yourMessageTagA";
        final Message message = provider.newMessageBuilder()
            // Set topic for the current message.
            .setTopic(topic)
            // Message secondary classifier of message besides topic.
            .setTag(tag)
            // Key(s) of the message, another way to mark message besides message id.
            .setKeys("yourMessageKey-1c151062f96e")
            .setBody(body)
            .build();
        try {
            final SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (Throwable t) {
            log.error("Failed to send message", t);
        }
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        // producer.close();

    }

}
