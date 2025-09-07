package com.yangshengzhou.lucky_sms.service;

import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.vo.LoginVO;
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

    public LoginVO login(String username, String keyhash) {
        // 1. 参数校验
        if (Objects.equals(username, "") || Objects.equals(keyhash, "")) {
            // 如果传到后端的任意一个为空则抛出错误
            throw new IllegalArgumentException("手机号或密码不能为空");
        }

        // 2. 调用Mapper查询（直接返回包含角色的LoginVO）
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("passwordHash", keyhash); // 注意参数名与mapper的XML一致
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

    // 手机号脱敏
    private String desensitizePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}