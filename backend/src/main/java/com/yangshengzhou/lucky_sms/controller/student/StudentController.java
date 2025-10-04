package com.yangshengzhou.lucky_sms.controller.student;

import com.yangshengzhou.lucky_sms.service.student.GradeService;
import com.yangshengzhou.lucky_sms.service.student.HomeService;
import com.yangshengzhou.lucky_sms.service.student.StatusService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
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
    @Resource
    private StatusService statusService;
    @Resource
    private GradeService gradeService;

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
            StatusVO statusDate = statusService.getStatusDate(userId);

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

    @GetMapping("/grades")
    public HashMap<String, Object> studentGradesResult(
            HttpServletRequest request
    ) {
        HashMap<String, Object> studentGradesResult = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeService.getGradesData(userId);

            studentGradesResult.put("code", 200);
            studentGradesResult.put("message", "请求成功");
            studentGradesResult.put("data", gradesData);
        } catch (Exception e) {
            studentGradesResult.put("code", 500);
            studentGradesResult.put("message", e.getMessage());
            studentGradesResult.put("data", null);
        }

        return studentGradesResult;
    }

    @GetMapping("/grades/{semester}")
    public HashMap<String, Object> studentGradesBySemesterResult(
            @PathVariable String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> studentGradesResult = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeService.getGradesDataBySemester(userId, semester);

            studentGradesResult.put("code", 200);
            studentGradesResult.put("message", "请求成功");
            studentGradesResult.put("data", gradesData);
        } catch (Exception e) {
            studentGradesResult.put("code", 500);
            studentGradesResult.put("message", e.getMessage());
            studentGradesResult.put("data", null);
        }

        return studentGradesResult;
    }

    @GetMapping("/grades/pagination")
    public HashMap<String, Object> studentGradesWithPaginationResult(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> studentGradesResult = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeService.getGradesDataWithPagination(userId, page, size);

            studentGradesResult.put("code", 200);
            studentGradesResult.put("message", "请求成功");
            studentGradesResult.put("data", gradesData);
        } catch (Exception e) {
            studentGradesResult.put("code", 500);
            studentGradesResult.put("message", e.getMessage());
            studentGradesResult.put("data", null);
        }

        return studentGradesResult;
    }

    @GetMapping("/grades/{semester}/pagination")
    public HashMap<String, Object> studentGradesBySemesterWithPaginationResult(
            @PathVariable String semester,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> studentGradesResult = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeService.getGradesDataBySemesterWithPagination(userId, semester, page, size);

            studentGradesResult.put("code", 200);
            studentGradesResult.put("message", "请求成功");
            studentGradesResult.put("data", gradesData);
        } catch (Exception e) {
            studentGradesResult.put("code", 500);
            studentGradesResult.put("message", e.getMessage());
            studentGradesResult.put("data", null);
        }

        return studentGradesResult;
    }
}
