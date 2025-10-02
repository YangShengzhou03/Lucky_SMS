package com.yangshengzhou.lucky_sms.controller.Student;

import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/home")
    public HashMap<String, Object> studentHomeResult(
            HttpServletRequest request) { // 用于获取请求头

        HashMap<String, Object> result = new HashMap<>();

        try {
            // 1. 从请求头中获取 Authorization（Token 通常在这里）
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                result.put("code", 401);
                result.put("message", "请携带有效的 Token（格式：Bearer <token>）");
                return result;
            }

            // 2. 提取纯 Token（去掉 "Bearer " 前缀）
            String token = authorization.substring(7);

            // 3. 验证 Token 有效性
            if (!jwtUtil.validateToken(token)) {
                result.put("code", 401);
                result.put("message", "Token 无效或已过期");
                return result;
            }

            // 4. 从 Token 中解析用户 ID
            int userId = jwtUtil.getUserIdFromToken(token);

            // 5. 业务逻辑处理（例如根据 userId 和 name 返回数据）
            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", new HashMap<String, Object>() {{
                put("userId", userId);       // Token 解析出的用户 ID
                put("message", "欢迎回来，用户 " + userId);
            }});

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }
}
