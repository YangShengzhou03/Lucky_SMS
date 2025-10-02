package com.yangshengzhou.lucky_sms.mapper;

import com.yangshengzhou.lucky_sms.vo.LoginVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 通过手机号查找用户
    LoginVO loginByPhone(String phone);

    // 通过手机号注册用户
    int registerByPhone(String phone);

    // 通过手机号重设密码
    int resetPasswordByPhone(String phone, String password);
}