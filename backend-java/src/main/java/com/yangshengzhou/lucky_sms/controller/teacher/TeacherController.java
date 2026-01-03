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

    @GetMapping("/home")
    public HashMap<String, Object> teacherHomeResult(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            HomeVO homeVO = teacherHomeService.getHomeData(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", homeVO);
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

    @GetMapping("/profile")
    public HashMap<String, Object> getTeacherProfile(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            TeacherProfileVO profile = teacherProfileService.getTeacherProfile(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", profile);
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

    @PostMapping("/profile")
    public HashMap<String, Object> updateTeacherProfile(
            @RequestBody TeacherProfileVO profile,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            boolean success = teacherProfileService.updateTeacherProfile(userId, profile);

            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
                result.put("data", null);
            } else {
                result.put("code", 500);
                result.put("message", "更新失败");
                result.put("data", null);
            }
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

    @GetMapping("/grades")
    public HashMap<String, Object> getStudentGrades(
            @RequestParam Integer courseId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradeManagementVO gradeVO = teacherGradeService.getCourseGrades(courseId, userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", gradeVO);
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

    @PostMapping("/grades")
    public HashMap<String, Object> updateStudentGrade(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            Integer gradeId = (Integer) data.get("gradeId");
            Double usualScore = data.get("usualScore") != null ? ((Number) data.get("usualScore")).doubleValue() : null;
            Double midtermScore = data.get("midtermScore") != null ? ((Number) data.get("midtermScore")).doubleValue() : null;
            Double finalScore = data.get("finalScore") != null ? ((Number) data.get("finalScore")).doubleValue() : null;

            boolean success = teacherGradeService.updateGrade(gradeId, usualScore, midtermScore, finalScore, userId);

            if (success) {
                result.put("code", 200);
                result.put("message", "成绩录入成功");
                result.put("data", null);
            } else {
                result.put("code", 500);
                result.put("message", "成绩录入失败");
                result.put("data", null);
            }
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

    @GetMapping("/schedule")
    public HashMap<String, Object> getSchedule(
            @RequestParam(defaultValue = "2024-2025-第一学期") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            ScheduleVO schedule = teacherScheduleService.getSchedule(semester, userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", schedule);
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
