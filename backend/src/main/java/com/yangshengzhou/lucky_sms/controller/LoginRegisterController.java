package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginRegisterController {

    @PostMapping("/phone")
    public Map<String, Object> loginPhone(
            @RequestParam String phone,
            @RequestParam String captcha) {

        Map<String, Object> loginResult = new HashMap<>();
        LoginVO loginVO = new LoginVO();

        loginResult.put("code", 500);
        loginResult.put("message", "服务器处理出错");
        loginResult.put("data", loginVO);
        return loginResult;
    }
}
