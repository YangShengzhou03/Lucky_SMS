package com.yangshengzhou.lucky_sms.service;

import com.yangshengzhou.lucky_sms.config.RocketMQConfig;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class RocketMQProducerService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendCourseSelectionSuccessMessage(Object message) {
        String destination = RocketMQConfig.COURSE_SELECTION_TOPIC + ":" + RocketMQConfig.SELECTION_SUCCESS_TAG;
        rocketMQTemplate.convertAndSend(destination, message);
    }

    public void sendDropCourseSuccessMessage(Object message) {
        String destination = RocketMQConfig.COURSE_SELECTION_TOPIC + ":" + RocketMQConfig.DROP_SUCCESS_TAG;
        rocketMQTemplate.convertAndSend(destination, message);
    }

    public void sendUserRegisterMessage(Object message) {
        String destination = RocketMQConfig.USER_OPERATION_TOPIC + ":" + RocketMQConfig.USER_REGISTER_TAG;
        rocketMQTemplate.convertAndSend(destination, message);
    }

    public void sendUserLoginMessage(Object message) {
        String destination = RocketMQConfig.USER_OPERATION_TOPIC + ":" + RocketMQConfig.USER_LOGIN_TAG;
        rocketMQTemplate.convertAndSend(destination, message);
    }

    public void sendAsyncMessage(String topic, String tag, Object message, SendCallback callback) {
        String destination = topic + ":" + tag;
        Message<?> msg = MessageBuilder.withPayload(message).build();
        rocketMQTemplate.asyncSend(destination, msg, callback);
    }
}
