package com.yangshengzhou.lucky_sms.controller.student;

import com.yangshengzhou.lucky_sms.service.student.HomeService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StatusVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private HomeService homeService;

    @GetMapping("/home")
    public HashMap<String, Object> studentHomeResult(
            HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            HomeVO homeVO = homeService.getHomeDate(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", homeVO);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    @GetMapping("/status")
    public HashMap<String, Object> studentStatusResult(
            HttpServletRequest request
    ) {
        HashMap<String, Object> studentStatusResult = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            StatusVO statusDate = homeService.getStatusDate(userId);

            studentStatusResult.put("code", 200);
            studentStatusResult.put("message", "请求成功");
            studentStatusResult.put("data", statusDate);
        } catch (Exception e) {
            studentStatusResult.put("code", 500);
            studentStatusResult.put("message", e.getMessage());
            studentStatusResult.put("data", null);
        }

        return studentStatusResult;
    }
}
