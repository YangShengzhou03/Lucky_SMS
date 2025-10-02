package com.yangshengzhou.lucky_sms.mapper;

import com.yangshengzhou.lucky_sms.vo.LoginVO;
import com.yangshengzhou.lucky_sms.vo.RegisterVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface UserMapper {
    // 直接返回LoginVO（包含角色信息），不再返回User实体
    LoginVO login(Map<String, Object> params);

    // 检查该手机号的用户是否已存在
    boolean isUsedPhone(String phone);

    // 通过用户名和手机号新建用户
    RegisterVO register(Map<String, Object> params);

    // 通过手机号查找用户
    LoginVO loginByPhone(String phone);

    // 通过手机号注册用户
    int registerByPhone(String phone);
}