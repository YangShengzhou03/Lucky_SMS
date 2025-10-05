package com.yangshengzhou.lucky_sms.controller.student;

import com.yangshengzhou.lucky_sms.service.student.CourseSelectionService;
import com.yangshengzhou.lucky_sms.service.student.GradeService;
import com.yangshengzhou.lucky_sms.service.student.HomeService;
import com.yangshengzhou.lucky_sms.service.student.StatusService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StatusVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


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
    @Resource
    private CourseSelectionService courseSelectionService;

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
