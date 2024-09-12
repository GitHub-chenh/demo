package com.chenh.rocketmq.service;

import com.chenh.rocketmq.config.RocketmqConflg;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
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
    public boolean getNormalMessageProvider(
            String topic,String messageBody,String tag,String keys){

        ClientConfiguration configuration = ClientConfiguration.newBuilder()
                .setEndpoints(conflg.getNameServer())
                .build();
        Producer producer = null;
        try {
            producer = provider.newProducerBuilder()
                    .setTopics(topic)
                    .setClientConfiguration(configuration)
                    .build();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        // Define your message body.
        byte[] body = messageBody.getBytes(StandardCharsets.UTF_8);

        Message message = provider.newMessageBuilder()
            .setTopic(topic)
            .setTag(tag)
//            // Key(s) of the message, another way to mark message besides message id.
            .setKeys(keys)
            .setBody(body)
            .build();
        try {
            final SendReceipt sendReceipt = producer.send(message);
            log.info("消息发送成功, messageId={}", sendReceipt.getMessageId());

        } catch (Throwable t) {
            log.error("消息发送失败", t);
            return false;
        }
        return true;
        // Close the producer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        // producer.close();

    }

}
