package com.yangshengzhou.lucky_sms.controller.admin;

import com.yangshengzhou.lucky_sms.service.admin.AdminService;
import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import com.yangshengzhou.lucky_sms.vo.admin.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private AdminService adminService;

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

    @PostMapping("/users")
    public HashMap<String, Object> addUser(@RequestBody HashMap<String, Object> userData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.addUser(userData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", userData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PutMapping("/users")
    public HashMap<String, Object> updateUser(@RequestBody HashMap<String, Object> userData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.updateUser(userData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", userData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
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

    @PostMapping("/courses")
    public HashMap<String, Object> addCourse(@RequestBody HashMap<String, Object> courseData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.addCourse(courseData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", courseData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PutMapping("/courses")
    public HashMap<String, Object> updateCourse(@RequestBody HashMap<String, Object> courseData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.updateCourse(courseData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", courseData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
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

    @PutMapping("/grades")
    public HashMap<String, Object> updateGrade(@RequestBody HashMap<String, Object> gradeData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.updateGrade(gradeData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", gradeData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @DeleteMapping("/grades/{gradeId}")
    public HashMap<String, Object> deleteGrade(@PathVariable Integer gradeId) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            result.put("code", 200);
            result.put("message", "删除成绩成功");
            result.put("data", true);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
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

    @PostMapping("/departments")
    public HashMap<String, Object> addDepartment(@RequestBody HashMap<String, Object> deptData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.addDepartment(deptData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", deptData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PutMapping("/departments")
    public HashMap<String, Object> updateDepartment(@RequestBody HashMap<String, Object> deptData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.updateDepartment(deptData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", deptData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/teachers")
    public HashMap<String, Object> getTeacherList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.getTeacherList(page, size, keyword);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", serviceResult);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/announcements")
    public HashMap<String, Object> getAnnouncementList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String department) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.getAnnouncementList(page, size, keyword, type, department);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", serviceResult);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PostMapping("/announcements")
    public HashMap<String, Object> createAnnouncement(@RequestBody HashMap<String, Object> announcementData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.createAnnouncement(announcementData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", announcementData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PutMapping("/announcements")
    public HashMap<String, Object> updateAnnouncement(@RequestBody HashMap<String, Object> announcementData) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.updateAnnouncement(announcementData);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", announcementData);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @DeleteMapping("/announcements/{announcementId}")
    public HashMap<String, Object> deleteAnnouncement(@PathVariable Integer announcementId) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            HashMap<String, Object> serviceResult = adminService.deleteAnnouncement(announcementId);
            result.put("code", 200);
            result.put("message", serviceResult.get("message"));
            result.put("data", serviceResult.get("success"));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }

        return result;
    }
}
