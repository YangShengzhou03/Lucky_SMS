package com.yangshengzhou.lucky_sms.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消费者配置类
 * 添加条件配置，当RocketMQ可用时才启动消费者
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "rocketmq.consumer.enabled", havingValue = "true", matchIfMissing = false)
public class RocketMQConsumerConfig {

    /**
     * 课程选择消费者
     */
    @Component
    @RocketMQMessageListener(
        topic = RocketMQConfig.COURSE_SELECTION_TOPIC, 
        consumerGroup = "course-selection-consumer-group",
        enableMsgTrace = false
    )
    @ConditionalOnProperty(name = "rocketmq.consumer.course-selection.enabled", havingValue = "true", matchIfMissing = false)
    public static class CourseSelectionConsumer implements RocketMQListener<Object> {
        @Override
        public void onMessage(Object message) {
            log.info("收到课程选择消息: {}", message);
        }
    }

    /**
     * 用户操作消费者
     */
    @Component
    @RocketMQMessageListener(
        topic = RocketMQConfig.USER_OPERATION_TOPIC, 
        consumerGroup = "user-operation-consumer-group",
        enableMsgTrace = false
    )
    @ConditionalOnProperty(name = "rocketmq.consumer.user-operation.enabled", havingValue = "true", matchIfMissing = false)
    public static class UserOperationConsumer implements RocketMQListener<Object> {
        @Override
        public void onMessage(Object message) {
            log.info("收到用户操作消息: {}", message);
        }
    }
}