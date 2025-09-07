package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(
            @RequestParam String username,
            @RequestParam String phone,
            @RequestParam String captcha) {
        Map<String, Object> registerResult = new HashMap<String, Object>();

        // TODO 验证码验证逻辑



    }

}
