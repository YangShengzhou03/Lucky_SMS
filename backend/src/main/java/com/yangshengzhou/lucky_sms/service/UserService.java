package com.yangshengzhou.lucky_sms.service;

import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.user.LoginVO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    public LoginVO loginByPhone(String phone) {
        LoginVO loginVO = userMapper.loginByPhone(phone);

        if (loginVO == null) {
            int rowNum = userMapper.registerByPhone(phone);
            if (rowNum == 0) {
                throw new RuntimeException("注册失败");
            }
        }
        loginVO = userMapper.loginByPhone(phone);

        String token = jwtUtil.generateToken(loginVO.getUid());
        loginVO.setToken(token);

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
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(loginVO.getUid());
        loginVO.setToken(token);

        loginVO.setPhone(desensitizePhone(loginVO.getPhone()));

        return loginVO;
    }

    private String desensitizePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}