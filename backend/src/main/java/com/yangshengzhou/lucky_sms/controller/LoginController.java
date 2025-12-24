package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.service.RocketMQProducerService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.user.LoginVO;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RocketMQProducerService rocketMQProducerService;

    @PostMapping("/login/password")
    public HashMap<String, Object> loginByPassword(
            @RequestParam String phone,
            @RequestParam String password) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            if (phone == null || password == null) {
                result.put("code", 400);
                result.put("message", "手机号和密码不能为空");
                result.put("data", null);
                return result;
            }

            LoginVO loginVO = userMapper.loginByPassword(phone, password);

            if (loginVO == null) {
                result.put("code", 401);
                result.put("message", "手机号或密码错误");
                result.put("data", null);
                return result;
            }

            String token = jwtUtil.generateToken(loginVO.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", loginVO.getId());
            data.put("username", loginVO.getUsername());
            data.put("role", loginVO.getRoleName());
            data.put("studentId", loginVO.getStudentId());

            Map<String, Object> loginMessage = new HashMap<>();
            loginMessage.put("userId", loginVO.getId());
            loginMessage.put("username", loginVO.getUsername());
            loginMessage.put("role", loginVO.getRoleName());
            loginMessage.put("loginTime", System.currentTimeMillis());
            rocketMQProducerService.sendUserLoginMessage(loginMessage);

            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PostMapping("/login/phone")
    public HashMap<String, Object> loginByPhone(
            @RequestParam String phone,
            @RequestParam String captcha) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            if (phone == null || captcha == null) {
                result.put("code", 400);
                result.put("message", "手机号和验证码不能为空");
                result.put("data", null);
                return result;
            }

            LoginVO loginVO = userMapper.loginByPhone(phone);

            if (loginVO == null) {
                result.put("code", 401);
                result.put("message", "用户不存在");
                result.put("data", null);
                return result;
            }

            String token = jwtUtil.generateToken(loginVO.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", loginVO.getId());
            data.put("username", loginVO.getUsername());
            data.put("role", loginVO.getRoleName());
            data.put("studentId", loginVO.getStudentId());

            Map<String, Object> loginMessage = new HashMap<>();
            loginMessage.put("userId", loginVO.getId());
            loginMessage.put("username", loginVO.getUsername());
            loginMessage.put("role", loginVO.getRoleName());
            loginMessage.put("loginTime", System.currentTimeMillis());
            rocketMQProducerService.sendUserLoginMessage(loginMessage);

            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PostMapping("/captcha/send")
    public HashMap<String, Object> sendCode(@RequestParam String phone) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            if (phone == null) {
                result.put("code", 400);
                result.put("message", "手机号不能为空");
                result.put("data", null);
                return result;
            }

            result.put("code", 200);
            result.put("message", "验证码已发送（测试环境：123456）");
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发送验证码失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PostMapping("/resetPassword")
    public HashMap<String, Object> resetPassword(
            @RequestParam String phone,
            @RequestParam String captcha,
            @RequestParam String newPassword) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            if (phone == null || newPassword == null || captcha == null) {
                result.put("code", 400);
                result.put("message", "手机号、新密码和验证码不能为空");
                result.put("data", null);
                return result;
            }

            int affectedRows = userMapper.resetPasswordByPhone(phone, newPassword);

            if (affectedRows > 0) {
                result.put("code", 200);
                result.put("message", "密码重置成功");
                result.put("data", null);
            } else {
                result.put("code", 404);
                result.put("message", "用户不存在");
                result.put("data", null);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "重置密码失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/getUserInfo")
    public HashMap<String, Object> getUserInfo(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "用户未登录");
                result.put("data", null);
                return result;
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("username", "用户" + userId);
            userInfo.put("role", "student");
            userInfo.put("date", userInfo);

            result.put("code", 200);
            result.put("message", "获取用户信息成功");
            result.put("data", userInfo);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取用户信息失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PostMapping("/logout")
    public HashMap<String, Object> logout() {
        HashMap<String, Object> result = new HashMap<>();

        try {
            result.put("code", 200);
            result.put("message", "退出登录成功");
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "退出登录失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }
}