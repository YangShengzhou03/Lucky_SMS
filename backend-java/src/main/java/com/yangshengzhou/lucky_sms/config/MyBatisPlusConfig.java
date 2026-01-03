package com.yangshengzhou.lucky_sms.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yangshengzhou.lucky_sms.mapper")
public class MyBatisPlusConfig {
}
