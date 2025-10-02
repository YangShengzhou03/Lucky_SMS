package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.service.UserService;
import com.yangshengzhou.lucky_sms.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginRegisterController {

    @Resource
    private UserService userService;

    @PostMapping("/phone")
    public Map<String, Object> loginByPhone(
            @RequestParam String phone,
            @RequestParam String captcha) {
        LoginVO loginVO = new LoginVO();
        // 封装返回Map
        Map<String, Object> loginResult = new HashMap<>();
        try {
            // TODO 对验证码进行校验
            if (captcha==null || !captcha.equals("123456")) {
                throw new RuntimeException("无效验证码");
            }
            // TODO 用户存在则登录，不存在则注册
            loginVO = userService.loginByPhone(phone);
            loginResult.put("code", 200);
            loginResult.put("message", "登录成功");
            loginResult.put("data", loginVO);
        } catch (Exception e) {
            loginResult.put("code", 500);
            loginResult.put("message", e.getMessage());
            loginResult.put("data", loginVO);
        }
        return loginResult;
    }

    @PostMapping("/password")
    public Map<String, Object> loginByPassword(
            @RequestParam String phone,
            @RequestParam String password
    ){
        HashMap<String, Object> loginResult = new HashMap<>();
        LoginVO loginVO = userService.loginByPassword(phone, password);
        try {
            loginResult.put("code", 200);
            loginResult.put("message", "登录成功");
            loginResult.put("date", loginVO);
        } catch (Exception e) {
            loginResult.put("code", 500);
            loginResult.put("message", e.getMessage());
            loginResult.put("date", loginVO);
        }
        return loginResult;
    }
}
