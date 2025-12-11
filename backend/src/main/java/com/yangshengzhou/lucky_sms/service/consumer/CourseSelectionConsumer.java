package com.yangshengzhou.lucky_sms.service.consumer;

import com.yangshengzhou.lucky_sms.config.RocketMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 选课消息消费者
 */
@Component
@RocketMQMessageListener(topic = RocketMQConfig.COURSE_SELECTION_TOPIC, consumerGroup = "course-selection-consumer-group")
public class CourseSelectionConsumer implements RocketMQListener<Object> {

    private static final Logger log = LoggerFactory.getLogger(CourseSelectionConsumer.class);

    @Override
    public void onMessage(Object message) {
        // 处理选课相关消息
        log.info("收到选课消息: {}", message.toString());
        
        // 这里可以根据消息类型进行不同的处理，比如记录日志、发送通知等
        // 实际应用中应该根据业务需求实现具体的处理逻辑
    }
}
