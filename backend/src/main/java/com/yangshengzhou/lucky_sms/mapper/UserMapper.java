package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangshengzhou.lucky_sms.pojo.User;
import com.yangshengzhou.lucky_sms.vo.user.LoginVO;

public interface UserMapper extends BaseMapper<User> {
    LoginVO loginByPhone(String phone);

    int registerByPhone(String phone);

    int resetPasswordByPhone(String phone, String password);

    LoginVO loginByPassword(String phone, String password);
}