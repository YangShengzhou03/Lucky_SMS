package com.yangshengzhou.lucky_sms.controller.user;

import com.yangshengzhou.lucky_sms.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ResetPasswordController {

    @Resource
    private UserService userService;

    @PostMapping("/resetPassword")
    public Map<String,Object> resetPassword(
            @RequestParam String phone,
            @RequestParam String captcha,
            @RequestParam String newPassword
    ){
        Map<String,Object> resetPasswordResult = new HashMap<>();
        if (captcha==null || !captcha.equals("123456")){
            throw new RuntimeException("无效验证码");
        }

        try {
            if (!userService.resetPassword(phone, newPassword)){
                throw new RuntimeException("密码重置失败");
            }
            resetPasswordResult.put("code", 200);
            resetPasswordResult.put("message", "重置成功");
            resetPasswordResult.put("data", null);
        } catch (Exception e) {
            resetPasswordResult.put("code", 500);
            resetPasswordResult.put("message", e.getMessage());
            resetPasswordResult.put("data", null);
        }
        return resetPasswordResult;
    }
}
