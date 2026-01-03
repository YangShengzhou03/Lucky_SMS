package com.yangshengzhou.lucky_sms.controller.student;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.service.student.CourseSelectionService;
import com.yangshengzhou.lucky_sms.service.student.ProfileService;
import com.yangshengzhou.lucky_sms.service.student.StudentScheduleService;
import com.yangshengzhou.lucky_sms.service.student.impl.GradeServiceImpl;
import com.yangshengzhou.lucky_sms.service.student.impl.HomeServiceImpl;
import com.yangshengzhou.lucky_sms.service.student.impl.StatusServiceImpl;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StatusVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentProfileVO;
import com.yangshengzhou.lucky_sms.vo.student.UpdateStudentProfileRequestVO;
import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    
    @Resource
    private ProfileService profileService;

    @Resource
    private StudentScheduleService studentScheduleService;

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


    @GetMapping("/courses/available/pagination")
    public HashMap<String, Object> getAvailableCoursesWithPagination(
            @RequestParam(defaultValue = "1") Integer semesterId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            Page<CourseSelectionVO> courses = courseSelectionService.getAvailableCoursesWithPagination(userId, semesterId, page, size);

            result.put("code", 200);
            result.put("message", "获取课程列表成功");
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


    @GetMapping("/courses/selected/pagination")
    public HashMap<String, Object> getSelectedCoursesWithPagination(
            @RequestParam(defaultValue = "1") Integer semesterId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            Page<CourseSelectionVO> courses = courseSelectionService.getSelectedCoursesWithPagination(userId, semesterId, page, size);

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
            @RequestParam Integer assignmentId,
            @RequestParam(defaultValue = "1") Integer selectionTypeId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            CourseSelectionResultVO selectionResult = courseSelectionService.selectCourse(userId, assignmentId, selectionTypeId);

            result.put("code", 200);
            result.put("message", "选课成功");
            result.put("data", selectionResult);
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
            @RequestParam Integer selectionId,
            @RequestParam(defaultValue = "个人原因") String dropReason,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            CourseSelectionResultVO dropResult = courseSelectionService.dropCourse(userId, selectionId, dropReason);

            result.put("code", 200);
            result.put("message", "退课成功");
            result.put("data", dropResult);
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

    @GetMapping("/profile")
    public HashMap<String, Object> getStudentProfile(HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<>();
        
        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            if (userId == null) {
                response.put("code", 401);
                response.put("message", "用户未登录");
                return response;
            }
            
            StudentProfileVO profile = profileService.getStudentProfile(userId);
            
            response.put("code", 200);
            response.put("message", "获取个人信息成功");
            response.put("data", profile);
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取个人信息失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
    
    @PostMapping("/profile")
    public HashMap<String, Object> updateStudentProfile(
            @RequestBody UpdateStudentProfileRequestVO requestVO,
            HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<>();
        
        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            if (userId == null) {
                response.put("code", 401);
                response.put("message", "用户未登录");
                return response;
            }

            boolean success = profileService.updateStudentProfile(userId, requestVO);
            
            if (success) {
                response.put("code", 200);
                response.put("message", "更新个人信息成功");
                response.put("data", null);
            } else {
                response.put("code", 500);
                response.put("message", "更新个人信息失败");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "更新个人信息失败: " + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }

    @GetMapping("/schedule")
    public HashMap<String, Object> getStudentSchedule(
            @RequestParam(defaultValue = "2024-2025-第一学期") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            ScheduleVO schedule = studentScheduleService.getSchedule(semester, userId);

            result.put("code", 200);
            result.put("message", "获取课表成功");
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
