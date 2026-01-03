package com.yangshengzhou.lucky_sms.controller.admin;

import com.yangshengzhou.lucky_sms.service.admin.AdminService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.admin.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private AdminService adminService;

    @GetMapping("/home")
    public HashMap<String, Object> adminHome(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            AdminHomeVO homeData = adminService.getHomeData(userId);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", homeData);
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

    @GetMapping("/users")
    public HashMap<String, Object> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            UserManagementVO userData = adminService.getUserList(page, size, keyword, role);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", userData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @DeleteMapping("/users/{userId}")
    public HashMap<String, Object> deleteUser(@PathVariable Integer userId) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            boolean success = adminService.deleteUser(userId);

            result.put("code", 200);
            result.put("message", success ? "删除成功" : "删除失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }

    @PutMapping("/users/{userId}/status")
    public HashMap<String, Object> updateUserStatus(
            @PathVariable Integer userId,
            @RequestBody HashMap<String, String> requestBody
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            String status = requestBody.get("status");
            boolean success = adminService.updateUserStatus(userId, status);

            result.put("code", 200);
            result.put("message", success ? "状态更新成功" : "状态更新失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }

    @GetMapping("/courses")
    public HashMap<String, Object> getCourseList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            CourseManagementVO courseData = adminService.getCourseList(page, size, keyword, department);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", courseData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @DeleteMapping("/courses/{courseId}")
    public HashMap<String, Object> deleteCourse(@PathVariable Integer courseId) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            boolean success = adminService.deleteCourse(courseId);

            result.put("code", 200);
            result.put("message", success ? "删除成功" : "删除失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }

    @PutMapping("/courses/{courseId}/status")
    public HashMap<String, Object> updateCourseStatus(
            @PathVariable Integer courseId,
            @RequestBody HashMap<String, String> requestBody
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            String status = requestBody.get("status");
            boolean success = adminService.updateCourseStatus(courseId, status);

            result.put("code", 200);
            result.put("message", success ? "状态更新成功" : "状态更新失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }

    @GetMapping("/grades")
    public HashMap<String, Object> getGradeList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String semester
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            GradeManagementVO gradeData = adminService.getGradeList(page, size, keyword, semester);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", gradeData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/departments")
    public HashMap<String, Object> getDepartmentList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            DepartmentManagementVO deptData = adminService.getDepartmentList(page, size, keyword);

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", deptData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @DeleteMapping("/departments/{deptId}")
    public HashMap<String, Object> deleteDepartment(@PathVariable Integer deptId) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            boolean success = adminService.deleteDepartment(deptId);

            result.put("code", 200);
            result.put("message", success ? "删除成功" : "删除失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }

    @PutMapping("/departments/{deptId}/status")
    public HashMap<String, Object> updateDepartmentStatus(
            @PathVariable Integer deptId,
            @RequestBody HashMap<String, String> requestBody
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            String status = requestBody.get("status");
            boolean success = adminService.updateDepartmentStatus(deptId, status);

            result.put("code", 200);
            result.put("message", success ? "状态更新成功" : "状态更新失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }

    @GetMapping("/settings")
    public HashMap<String, Object> getSystemSettings() {
        HashMap<String, Object> result = new HashMap<>();

        try {
            SystemSettingsVO settings = adminService.getSystemSettings();

            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", settings);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PutMapping("/settings")
    public HashMap<String, Object> updateSystemSettings(@RequestBody SystemSettingsVO settings) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            boolean success = adminService.updateSystemSettings(settings);

            result.put("code", 200);
            result.put("message", success ? "设置更新成功" : "设置更新失败");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }
}