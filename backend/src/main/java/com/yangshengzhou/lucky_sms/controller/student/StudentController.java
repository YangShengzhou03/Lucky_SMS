package com.yangshengzhou.lucky_sms.controller.student;

import com.yangshengzhou.lucky_sms.service.student.CourseSelectionService;
import com.yangshengzhou.lucky_sms.service.student.impl.GradeServiceImpl;
import com.yangshengzhou.lucky_sms.service.student.impl.HomeServiceImpl;
import com.yangshengzhou.lucky_sms.service.student.impl.StatusServiceImpl;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StatusVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private HomeServiceImpl homeServiceImpl;
    @Resource
    private StatusServiceImpl statusServiceImpl;
    @Resource
    private GradeServiceImpl gradeServiceImpl;
    @Resource
    private CourseSelectionService courseSelectionService;

    @GetMapping("/home")
    public HashMap<String, Object> studentHomeResult(
            HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            HomeVO homeVO = homeServiceImpl.getHomeDate(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", homeVO);
        } catch (ResponseStatusException e) {
            // 获取状态码（如 401）
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());  // 使用 getReason() 获取自定义消息
            result.put("data", null);
        } catch (Exception e) {  // 捕获其他所有异常（兜底）
            result.put("code", 500);  // 通用错误码
            result.put("message", e.getMessage());  // 其他异常的消息
            result.put("data", null);
        }
        return result;
    }

    @GetMapping("/status")
    public HashMap<String, Object> studentStatusResult(
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            StatusVO statusDate = statusServiceImpl.getStatusDate(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", statusDate);
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
    public HashMap<String, Object> studentGradesResult(
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeServiceImpl.getGradesData(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", gradesData);
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

    @GetMapping("/courses/available")
    public HashMap<String, Object> getAvailableCourses(
            @RequestParam(defaultValue = "2023-2024-2") String semester,
            HttpServletRequest request
    ) {
        // 响应可用课程
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            List<CourseSelectionVO> courses = courseSelectionService.getAvailableCourses(userId, semester);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", courses);
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

    @GetMapping("/courses/selected")
    public HashMap<String, Object> getSelectedCourses(
            @RequestParam(defaultValue = "2023-2024-2") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            List<CourseSelectionVO> courses = courseSelectionService.getSelectedCourses(userId, semester);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", courses);
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

    @PostMapping("/courses/select")
    public HashMap<String, Object> selectCourse(
            @RequestParam Integer courseId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            CourseSelectionResultVO selectionResult = courseSelectionService.selectCourse(userId, courseId);

            result.put("code", selectionResult.getSuccess() ? 200 : 400);
            result.put("message", selectionResult.getMessage());
            result.put("data", selectionResult.getCourse());
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

    @PostMapping("/courses/drop")
    public HashMap<String, Object> dropCourse(
            @RequestParam Integer courseId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            CourseSelectionResultVO dropResult = courseSelectionService.dropCourse(userId, courseId);

            result.put("code", dropResult.getSuccess() ? 200 : 400);
            result.put("message", dropResult.getMessage());
            result.put("data", dropResult.getCourse());
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

    @GetMapping("/grades/{semester}")
    public HashMap<String, Object> studentGradesBySemesterResult(
            @PathVariable String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeServiceImpl.getGradesDataBySemester(userId, semester);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", gradesData);
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

    @GetMapping("/grades/pagination")
    public HashMap<String, Object> studentGradesWithPaginationResult(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeServiceImpl.getGradesDataWithPagination(userId, page, size);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", gradesData);
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

    @GetMapping("/grades/{semester}/pagination")
    public HashMap<String, Object> studentGradesBySemesterWithPaginationResult(
            @PathVariable String semester,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            GradesVO gradesData = gradeServiceImpl.getGradesDataBySemesterWithPagination(userId, semester, page, size);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", gradesData);
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
