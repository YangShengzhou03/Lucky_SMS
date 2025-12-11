package com.yangshengzhou.lucky_sms.config;

import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ配置类
 */
@Configuration
public class RocketMQConfig {

    // 选课通知主题
    public static final String COURSE_SELECTION_TOPIC = "course-selection-topic";
    
    // 用户操作主题
    public static final String USER_OPERATION_TOPIC = "user-operation-topic";
    
    // 选课成功标签
    public static final String SELECTION_SUCCESS_TAG = "selection-success";
    
    // 退课成功标签
    public static final String DROP_SUCCESS_TAG = "drop-success";
    
    // 用户注册标签
    public static final String USER_REGISTER_TAG = "user-register";
    
    // 用户登录标签
    public static final String USER_LOGIN_TAG = "user-login";
}
