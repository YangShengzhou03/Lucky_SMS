package com.yangshengzhou.lucky_sms.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus配置类
 * 扫描所有mapper接口
 */
@Configuration
@MapperScan("com.yangshengzhou.lucky_sms.mapper")
public class MyBatisPlusConfig {
    // 基础Mapper接口可以在这里定义公共方法
}
