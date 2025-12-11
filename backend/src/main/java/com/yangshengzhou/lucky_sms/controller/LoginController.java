package com.yangshengzhou.lucky_sms.controller;

import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.service.RocketMQProducerService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.user.LoginVO;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RocketMQProducerService rocketMQProducerService;

    /**
     * 手机号密码登录
     * @param phone 手机号
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login/password")
    public HashMap<String, Object> loginByPassword(
            @RequestParam String phone,
            @RequestParam String password) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            // 参数已经通过@RequestParam获取

            if (phone == null || password == null) {
                result.put("code", 400);
                result.put("message", "手机号和密码不能为空");
                result.put("data", null);
                return result;
            }

            // 调用Mapper进行登录验证
            LoginVO loginVO = userMapper.loginByPassword(phone, password);

            if (loginVO == null) {
                result.put("code", 401);
                result.put("message", "手机号或密码错误");
                result.put("data", null);
                return result;
            }

            // 生成JWT token
            String token = jwtUtil.generateToken(loginVO.getId());

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", loginVO.getId());
            data.put("username", loginVO.getUsername());
            data.put("role", loginVO.getRoleName());
            data.put("studentId", loginVO.getStudentId());

            // 发送登录消息到消息队列
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

    /**
     * 手机号验证码登录
     * @param phone 手机号
     * @param captcha 验证码
     * @return 登录结果
     */
    @PostMapping("/login/phone")
    public HashMap<String, Object> loginByPhone(
            @RequestParam String phone,
            @RequestParam String captcha) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            // 参数已经通过@RequestParam获取

            if (phone == null || captcha == null) {
                result.put("code", 400);
                result.put("message", "手机号和验证码不能为空");
                result.put("data", null);
                return result;
            }

            // 这里应该验证验证码是否正确，暂时简化处理
            // TODO: 实现验证码验证逻辑

            // 调用Mapper查询用户信息
            LoginVO loginVO = userMapper.loginByPhone(phone);

            if (loginVO == null) {
                result.put("code", 401);
                result.put("message", "用户不存在");
                result.put("data", null);
                return result;
            }

            // 生成JWT token
            String token = jwtUtil.generateToken(loginVO.getId());

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", loginVO.getId());
            data.put("username", loginVO.getUsername());
            data.put("role", loginVO.getRoleName());
            data.put("studentId", loginVO.getStudentId());

            // 发送登录消息到消息队列
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

    /**
     * 发送验证码
     * @param phone 手机号
     * @return 发送结果
     */
    @PostMapping("/captcha/send")
    public HashMap<String, Object> sendCode(@RequestParam String phone) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            // phone参数已经通过@RequestParam获取

            if (phone == null) {
                result.put("code", 400);
                result.put("message", "手机号不能为空");
                result.put("data", null);
                return result;
            }

            // TODO: 实现发送验证码的逻辑
            // 这里应该调用短信发送服务发送验证码

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

    /**
     * 重置密码
     * @param phone 手机号
     * @param captcha 验证码
     * @param newPassword 新密码
     * @return 重置结果
     */
    @PostMapping("/resetPassword")
    public HashMap<String, Object> resetPassword(
            @RequestParam String phone,
            @RequestParam String captcha,
            @RequestParam String newPassword) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            // 参数已经通过@RequestParam获取

            if (phone == null || newPassword == null || captcha == null) {
                result.put("code", 400);
                result.put("message", "手机号、新密码和验证码不能为空");
                result.put("data", null);
                return result;
            }

            // TODO: 验证验证码

            // 重置密码
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

    /**
     * 获取当前用户信息
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/getUserInfo")
    public HashMap<String, Object> getUserInfo(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            // 从JWT token中获取用户ID
            Integer userId = jwtUtil.getUidByRequest(request);
            if (userId == null) {
                result.put("code", 401);
                result.put("message", "用户未登录");
                result.put("data", null);
                return result;
            }

            // 这里可以根据用户ID查询详细的用户信息
            // 构建返回数据
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("username", "用户" + userId);
            userInfo.put("role", "student"); // 暂时固定角色
            userInfo.put("date", userInfo); // 为了兼容前端的data字段

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

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    public HashMap<String, Object> logout() {
        HashMap<String, Object> result = new HashMap<>();

        try {
            // 由于使用JWT，服务端不需要做特殊处理
            // 客户端删除token即可
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