package com.yangshengzhou.lucky_sms.controller.teacher;

import com.yangshengzhou.lucky_sms.service.teacher.TeacherCourseService;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherGradeService;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherHomeService;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherProfileService;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherScheduleService;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherStudentService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.teacher.CourseManagementVO;
import com.yangshengzhou.lucky_sms.vo.teacher.GradeManagementVO;
import com.yangshengzhou.lucky_sms.vo.teacher.HomeVO;
import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;
import com.yangshengzhou.lucky_sms.vo.teacher.StudentManagementVO;
import com.yangshengzhou.lucky_sms.vo.teacher.TeacherProfileVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private TeacherHomeService teacherHomeService;

    @Resource
    private TeacherStudentService teacherStudentService;

    @Resource
    private TeacherCourseService teacherCourseService;

    @Resource
    private TeacherGradeService teacherGradeService;

    @Resource
    private TeacherScheduleService teacherScheduleService;

    @Resource
    private TeacherProfileService teacherProfileService;



    @GetMapping("/students")
    public HashMap<String, Object> getStudentsList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            StudentManagementVO studentVO = teacherStudentService.getStudentList(page, size, keyword, userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", studentVO);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/courses")
    public HashMap<String, Object> getCoursesList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "2024-2025-第一学期") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            CourseManagementVO courseVO = teacherCourseService.getCourseList(page, size, semester, userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", courseVO);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }





}
