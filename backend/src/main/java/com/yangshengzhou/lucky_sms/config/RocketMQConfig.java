package com.yangshengzhou.lucky_sms.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {

    public static final String COURSE_SELECTION_TOPIC = "course-selection-topic";
    
    public static final String USER_OPERATION_TOPIC = "user-operation-topic";
    
    public static final String SELECTION_SUCCESS_TAG = "selection-success";
    
    public static final String DROP_SUCCESS_TAG = "drop-success";
    
    public static final String USER_REGISTER_TAG = "user-register";
    
    public static final String USER_LOGIN_TAG = "user-login";
}
