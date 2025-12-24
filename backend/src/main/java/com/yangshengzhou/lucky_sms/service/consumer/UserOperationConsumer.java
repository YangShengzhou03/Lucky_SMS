package com.yangshengzhou.lucky_sms.service.consumer;

import com.yangshengzhou.lucky_sms.config.RocketMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = RocketMQConfig.USER_OPERATION_TOPIC, 
    consumerGroup = "user-operation-consumer-group",
    enableMsgTrace = false
)
@ConditionalOnProperty(name = "rocketmq.consumer.user-operation.enabled", havingValue = "true", matchIfMissing = false)
public class UserOperationConsumer implements RocketMQListener<Object> {

    private static final Logger log = LoggerFactory.getLogger(UserOperationConsumer.class);

    @Override
    public void onMessage(Object message) {
        log.info("收到用户操作消息: {}", message.toString());
    }
}
