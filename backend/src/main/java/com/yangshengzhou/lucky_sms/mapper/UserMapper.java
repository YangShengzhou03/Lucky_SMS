package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangshengzhou.lucky_sms.pojo.User;
import com.yangshengzhou.lucky_sms.vo.user.LoginVO;

/**
 * 用户Mapper接口
 */
public interface UserMapper extends BaseMapper<User> {
    // 通过手机号查找用户
    LoginVO loginByPhone(String phone);

    // 通过手机号注册用户
    int registerByPhone(String phone);

    // 通过手机号重设密码
    int resetPasswordByPhone(String phone, String password);

    // 通过手机号密码登录
    LoginVO loginByPassword(String phone, String password);
}