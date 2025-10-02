package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.service.UserService;
import com.yangshengzhou.lucky_sms.vo.RegisterVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(
            @RequestParam String username,
            @RequestParam String phone,
            @RequestParam String captcha) {

        System.out.println("收到用户名：" + username);
        System.out.println("收到手机号" + phone);
        System.out.println("收到验证码" + captcha);

        Map<String, Object> registerResult = new HashMap<>(); // 实例化一个返回对象
        try {
            RegisterVO registerVO = userService.register(username, phone);
            registerResult.put("code", 200);
            registerResult.put("message", "注册成功");
            registerResult.put("data", registerVO);
        } catch (Exception e){
            registerResult.put("code", 400);
            registerResult.put("message", e.getMessage());
            registerResult.put("data", null);
        }
        return registerResult;
    }

}
