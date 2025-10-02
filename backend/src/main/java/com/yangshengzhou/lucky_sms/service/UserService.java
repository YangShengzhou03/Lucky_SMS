package com.yangshengzhou.lucky_sms.service;

import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.vo.LoginVO;
import com.yangshengzhou.lucky_sms.vo.RegisterVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

        // 4. 生成令牌
        String token = UUID.randomUUID().toString();
        loginVO.setToken(token);

        // 5. 手机号脱敏（直接操作VO中的phone字段）
        loginVO.setPhone(desensitizePhone(loginVO.getPhone()));

        return loginVO;
    }

    public LoginVO login(String username, String passwordHash) {
        // 1. 参数校验
        if (Objects.equals(username, "") || Objects.equals(passwordHash, "")) {
            // 如果传到后端的任意一个为空则抛出错误
            throw new IllegalArgumentException("手机号或密码不能为空");
        }

        // 2. 调用Mapper查询（直接返回包含角色的LoginVO）
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("passwordHash", passwordHash); // 注意参数名与mapper的XML一致
        LoginVO loginVO = userMapper.login(params);

        // 3. 处理登录结果
        if (loginVO == null) {
            throw new RuntimeException("手机号或密码错误");
        }

        // 4. 生成令牌
        String token = UUID.randomUUID().toString();
        loginVO.setToken(token);

        // 5. 手机号脱敏（直接操作VO中的phone字段）
        loginVO.setPhone(desensitizePhone(loginVO.getPhone()));

        return loginVO;
    }

    public RegisterVO register(String username, String phone) {
        // 参数校验
        if (Objects.equals(username, "") || Objects.equals(phone, "")) {
            throw new IllegalArgumentException("用户名或手机号不能为空");
        }

        // 判断该手机号是否已被使用
        boolean isUsedPhone = userMapper.isUsedPhone(phone);
        if (isUsedPhone) {
            throw new RuntimeException("该手机号已被注册");
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("phone", phone);
        
        // 插入用户记录
        RegisterVO registerVO = userMapper.register(params);
        
        // 创建返回对象
        registerVO.setUsername(username);
        registerVO.setPhone(phone);
        
        // 生成令牌
        String token = UUID.randomUUID().toString();
        registerVO.setToken(token);

        // 手机号脱敏
        registerVO.setPhone(desensitizePhone(registerVO.getPhone()));

        return registerVO;
    }

    // 手机号脱敏方法
    private String desensitizePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}