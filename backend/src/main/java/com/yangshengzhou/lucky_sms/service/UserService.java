package com.yangshengzhou.lucky_sms.service;

import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.vo.LoginVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public LoginVO loginByPhone(String phone) {
        LoginVO loginVO = userMapper.loginByPhone(phone);

        if (loginVO == null) {
            // 用户不存在，需要注册
            int rowNum = userMapper.registerByPhone(phone);
            if (rowNum == 0) {
                throw new RuntimeException("注册失败");
            }
        }
        loginVO = userMapper.loginByPhone(phone);

        // 生成令牌
        String token = UUID.randomUUID().toString();
        loginVO.setToken(token);

        // 手机号脱敏（直接操作VO中的phone字段）
        loginVO.setPhone(desensitizePhone(loginVO.getPhone()));

        return loginVO;
    }

    public boolean resetPassword(String phone, String password) {
        int rowNum = userMapper.resetPasswordByPhone(phone, password);
        return rowNum != 0;
    }

    public LoginVO loginByPassword(String phone, String password) {
        LoginVO loginVO = userMapper.loginByPassword(phone, password);

        if (loginVO == null) {
            // 用户不存在，需要注册
            throw new RuntimeException("用户名或密码错误");
        }

        // 令牌
        String token = UUID.randomUUID().toString();
        loginVO.setToken(token);

        // 手机号脱敏
        loginVO.setPhone(desensitizePhone(loginVO.getPhone()));

        return loginVO;
    }

    // 手机号脱敏方法
    private String desensitizePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}