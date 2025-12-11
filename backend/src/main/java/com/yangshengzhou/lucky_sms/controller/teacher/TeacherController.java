package com.yangshengzhou.lucky_sms.controller.teacher;

import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.*;

/**
 * 教师功能控制器
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取教师首页数据
     * @param request HTTP请求
     * @return 首页数据
     */
    @GetMapping("/home")
    public HashMap<String, Object> getHomeData(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟首页数据
            Map<String, Object> homeData = new HashMap<>();
            homeData.put("courseCount", 5);
            homeData.put("studentCount", 120);
            homeData.put("unreadMessageCount", 3);
            homeData.put("recentGrades", new ArrayList<>());
            
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

    /**
     * 获取学生列表（带分页）
     * @param page 页码
     * @param size 每页大小
     * @param request HTTP请求
     * @return 学生列表
     */
    @GetMapping("/students")
    public HashMap<String, Object> getStudentsList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟学生数据
            List<Map<String, Object>> students = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                Map<String, Object> student = new HashMap<>();
                student.put("id", i);
                student.put("studentId", "2021000" + i);
                student.put("name", "学生" + i);
                student.put("major", "计算机科学与技术");
                student.put("className", "计科210" + (i % 3 + 1) + "班");
                student.put("contact", "1380013800" + i);
                students.add(student);
            }
            
            // 计算分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, students.size());
            List<Map<String, Object>> pageStudents = students.subList(start, end);
            
            // 构建分页响应
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageStudents);
            pageData.put("total", students.size());
            pageData.put("size", size);
            pageData.put("current", page);
            pageData.put("pages", (int) Math.ceil((double) students.size() / size));
            
            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", pageData);
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

    /**
     * 获取课程列表（带分页）
     * @param page 页码
     * @param size 每页大小
     * @param semester 学期
     * @param request HTTP请求
     * @return 课程列表
     */
    @GetMapping("/courses")
    public HashMap<String, Object> getCoursesList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "2023-2024-2") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟课程数据
            List<Map<String, Object>> courses = new ArrayList<>();
            String[] courseNames = {"数据结构", "算法分析", "操作系统", "计算机网络", "数据库系统"};
            String[] courseTypes = {"必修课", "必修课", "必修课", "必修课", "选修课"};
            
            for (int i = 1; i <= 20; i++) {
                Map<String, Object> course = new HashMap<>();
                course.put("id", i);
                course.put("courseCode", "CS" + 2000 + i);
                course.put("courseName", courseNames[i % courseNames.length]);
                course.put("courseType", courseTypes[i % courseTypes.length]);
                course.put("credits", 3);
                course.put("classTime", "周一 3-4节, 周三 1-2节");
                course.put("location", "教1-20" + (i % 5 + 1));
                course.put("studentCount", 40 + (i % 30));
                courses.add(course);
            }
            
            // 计算分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, courses.size());
            List<Map<String, Object>> pageCourses = courses.subList(start, end);
            
            // 构建分页响应
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageCourses);
            pageData.put("total", courses.size());
            pageData.put("size", size);
            pageData.put("current", page);
            pageData.put("pages", (int) Math.ceil((double) courses.size() / size));
            
            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", pageData);
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

    /**
     * 获取教师个人信息
     * @param request HTTP请求
     * @return 教师个人信息
     */
    @GetMapping("/profile")
    public HashMap<String, Object> getTeacherProfile(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟教师信息
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", userId);
            profile.put("teacherId", "T" + 10000 + userId);
            profile.put("name", "张老师");
            profile.put("gender", "男");
            profile.put("birthDate", "1980-01-15");
            profile.put("title", "副教授");
            profile.put("department", "计算机科学与技术学院");
            profile.put("email", "zhang@example.com");
            profile.put("phone", "13900139000");
            profile.put("education", "博士研究生");
            profile.put("hireDate", "2010-09-01");
            
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

    /**
     * 更新教师个人信息
     * @param data 更新数据
     * @param request HTTP请求
     * @return 更新结果
     */
    @PostMapping("/profile")
    public HashMap<String, Object> updateTeacherProfile(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟更新操作
            result.put("code", 200);
            result.put("message", "更新成功");
            result.put("data", null);
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

    /**
     * 获取学生成绩
     * @param courseId 课程ID
     * @param request HTTP请求
     * @return 学生成绩列表
     */
    @GetMapping("/grades")
    public HashMap<String, Object> getStudentGrades(
            @RequestParam Integer courseId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟成绩数据
            List<Map<String, Object>> grades = new ArrayList<>();
            for (int i = 1; i <= 40; i++) {
                Map<String, Object> grade = new HashMap<>();
                grade.put("studentId", "2021000" + i);
                grade.put("studentName", "学生" + i);
                grade.put("attendance", 95 - (i % 10));
                grade.put("midterm", 70 + (i % 30));
                grade.put("final", 75 + (i % 25));
                grade.put("total", 75 + (i % 25));
                grade.put("letterGrade", getLetterGrade(75 + (i % 25)));
                grades.add(grade);
            }
            
            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", grades);
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

    /**
     * 录入/更新学生成绩
     * @param data 成绩数据
     * @param request HTTP请求
     * @return 更新结果
     */
    @PostMapping("/grades")
    public HashMap<String, Object> updateStudentGrade(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟更新操作
            result.put("code", 200);
            result.put("message", "成绩录入成功");
            result.put("data", null);
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

    /**
     * 获取课程表
     * @param semester 学期
     * @param request HTTP请求
     * @return 课程表数据
     */
    @GetMapping("/schedule")
    public HashMap<String, Object> getSchedule(
            @RequestParam(defaultValue = "2023-2024-2") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟课程表数据
            Map<String, Object> schedule = new HashMap<>();
            List<Map<String, Object>> classes = new ArrayList<>();
            
            // 周一课程
            classes.add(createClassData(1, 1, "数据结构", "教1-201", "计科2101班", 40));
            classes.add(createClassData(1, 3, "算法分析", "教2-303", "计科2102班", 35));
            
            // 周二课程
            classes.add(createClassData(2, 2, "操作系统", "教1-305", "计科2103班", 42));
            
            // 周三课程
            classes.add(createClassData(3, 1, "数据结构", "教1-201", "计科2101班", 40));
            classes.add(createClassData(3, 4, "数据库系统", "教2-207", "计科2102班", 38));
            
            // 周四课程
            classes.add(createClassData(4, 2, "操作系统", "教1-305", "计科2103班", 42));
            classes.add(createClassData(4, 5, "计算机网络", "教3-101", "网络2101班", 30));
            
            // 周五课程
            classes.add(createClassData(5, 3, "计算机网络", "教3-101", "网络2101班", 30));
            
            schedule.put("classes", classes);
            
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

    /**
     * 获取消息列表
     * @param page 页码
     * @param size 每页大小
     * @param request HTTP请求
     * @return 消息列表
     */
    @GetMapping("/messages")
    public HashMap<String, Object> getMessages(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟消息数据
            List<Map<String, Object>> messages = new ArrayList<>();
            String[] messageContents = {
                "请及时录入期中考试成绩",
                "下周将进行教学评估，请做好准备",
                "学生张三请假一周",
                "课程调整通知",
                "新的教学计划已发布"
            };
            
            for (int i = 1; i <= 15; i++) {
                Map<String, Object> message = new HashMap<>();
                message.put("id", i);
                message.put("sender", i % 2 == 0 ? "系统" : "教务处");
                message.put("content", messageContents[i % messageContents.length]);
                message.put("sendTime", new Date(System.currentTimeMillis() - i * 86400000).toString());
                message.put("isRead", i > 3);
                messages.add(message);
            }
            
            // 计算分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, messages.size());
            List<Map<String, Object>> pageMessages = messages.subList(start, end);
            
            // 构建分页响应
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageMessages);
            pageData.put("total", messages.size());
            pageData.put("size", size);
            pageData.put("current", page);
            pageData.put("pages", (int) Math.ceil((double) messages.size() / size));
            
            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", pageData);
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

    /**
     * 发送消息
     * @param data 消息数据
     * @param request HTTP请求
     * @return 发送结果
     */
    @PostMapping("/messages")
    public HashMap<String, Object> sendMessage(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟发送操作
            result.put("code", 200);
            result.put("message", "消息发送成功");
            result.put("data", null);
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

    /**
     * 标记消息已读
     * @param messageId 消息ID
     * @param request HTTP请求
     * @return 标记结果
     */
    @PostMapping("/messages/{messageId}/read")
    public HashMap<String, Object> markMessageRead(
            @PathVariable Integer messageId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            // 模拟标记操作
            result.put("code", 200);
            result.put("message", "消息已标记为已读");
            result.put("data", null);
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

    // 辅助方法：创建课程表数据
    private Map<String, Object> createClassData(int dayOfWeek, int section, String courseName, String location, String className, int studentCount) {
        Map<String, Object> classData = new HashMap<>();
        classData.put("dayOfWeek", dayOfWeek);
        classData.put("section", section);
        classData.put("courseName", courseName);
        classData.put("location", location);
        classData.put("className", className);
        classData.put("studentCount", studentCount);
        return classData;
    }

    // 辅助方法：根据分数获取等级
    private String getLetterGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}