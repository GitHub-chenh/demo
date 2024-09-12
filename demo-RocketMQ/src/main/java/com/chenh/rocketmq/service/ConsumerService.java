package com.chenh.rocketmq.service;

import com.chenh.rocketmq.config.RocketmqConflg;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.*;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    private final ClientServiceProvider provider = ClientServiceProvider.loadService();

    private final RocketmqConflg conflg;

    /**
     * 简单消费者
     */
    public boolean getSimpleMessageConsumer(String topic,String tag,String keys) throws ClientException {
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(conflg.getNameServer())
                .build();
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);

        SimpleConsumer consumer = provider.newSimpleConsumerBuilder()
                .setConsumerGroup(conflg.getConsumerGroup())
                .setClientConfiguration(clientConfiguration)
                .setAwaitDuration(Duration.ofSeconds(30))
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                .build();

        // Max message num for each long polling.
        int maxMessageNum = 16;
        // Set message invisible duration after it is received.
        Duration invisibleDuration = Duration.ofSeconds(15);
        // Receive message, multi-threading is more recommended.
        do {
            final List<MessageView> messages = consumer.receive(maxMessageNum, invisibleDuration);
            log.info("Received {} message(s)", messages.size());
            for (MessageView message : messages) {
                final MessageId messageId = message.getMessageId();
                try {
                    consumer.ack(message);
                    log.info("Message is acknowledged successfully, messageId={}", messageId);
                } catch (Throwable t) {
                    log.error("Message is failed to be acknowledged, messageId={}", messageId, t);
                }
            }
        } while (true);
        // Close the simple consumer when you don't need it anymore.
        // You could close it manually or add this into the JVM shutdown hook.
        // consumer.close();
    }
}
