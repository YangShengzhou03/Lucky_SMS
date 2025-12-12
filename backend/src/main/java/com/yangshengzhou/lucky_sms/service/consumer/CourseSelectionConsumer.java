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
    topic = RocketMQConfig.COURSE_SELECTION_TOPIC, 
    consumerGroup = "course-selection-consumer-group",
    enableMsgTrace = false
)
@ConditionalOnProperty(name = "rocketmq.consumer.course-selection.enabled", havingValue = "true", matchIfMissing = false)
public class CourseSelectionConsumer implements RocketMQListener<Object> {

    private static final Logger log = LoggerFactory.getLogger(CourseSelectionConsumer.class);

    @Override
    public void onMessage(Object message) {
        log.info("收到选课消息: {}", message.toString());
    }
}
