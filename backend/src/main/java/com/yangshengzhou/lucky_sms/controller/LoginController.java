package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.service.UserService;
import com.yangshengzhou.lucky_sms.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestParam String username,
            @RequestParam String keyhash
    ) {
        System.out.println("接收到：" + username);
        System.out.println("密码：" + keyhash);
        Map<String, Object> loginResult = new HashMap<>();
        try {
            LoginVO loginVO = userService.login(username, keyhash);
            loginResult.put("code", 200);       // 状态码：200成功
            loginResult.put("message", "登录成功");
            loginResult.put("data", loginVO);   // 响应数据
        } catch (Exception e) {
            loginResult.put("code", 400);       // 状态码：400失败
            loginResult.put("message", e.getMessage());
            loginResult.put("data", null);
        }
        System.out.println(loginResult);
        return loginResult;
    }
}