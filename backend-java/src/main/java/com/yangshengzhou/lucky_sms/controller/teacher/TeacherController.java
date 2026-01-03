package com.yangshengzhou.lucky_sms.controller.teacher;

import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Resource;
import java.util.*;

/**
     * 教师功能控制器
     */
    @RestController
    @RequestMapping("/teacher")
    public class TeacherController {

        @Resource
        private JwtUtil jwtUtil;

        @GetMapping("/home")
        public HashMap<String, Object> teacherHomeResult(HttpServletRequest request) {
            HashMap<String, Object> result = new HashMap<>();

            try {
                Integer userId = jwtUtil.getUidByRequest(request);

                HashMap<String, Object> homeData = new HashMap<>();
                homeData.put("teacherName", "张教授");
                homeData.put("teacherId", userId);
                homeData.put("totalStudents", 156);
                homeData.put("totalCourses", 8);
                homeData.put("thisSemesterCourses", 3);
                homeData.put("recentMessages", 5);
                homeData.put("pendingTasks", 2);

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

    @GetMapping("/students")
    public HashMap<String, Object> getStudentsList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, students.size());
            List<Map<String, Object>> pageStudents = students.subList(start, end);
            
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
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, courses.size());
            List<Map<String, Object>> pageCourses = courses.subList(start, end);
            
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

    @GetMapping("/profile")
    public HashMap<String, Object> getTeacherProfile(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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

    @PostMapping("/profile")
    public HashMap<String, Object> updateTeacherProfile(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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

    @GetMapping("/grades")
    public HashMap<String, Object> getStudentGrades(
            @RequestParam Integer courseId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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

    @PostMapping("/grades")
    public HashMap<String, Object> updateStudentGrade(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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

    @GetMapping("/schedule")
    public HashMap<String, Object> getSchedule(
            @RequestParam(defaultValue = "2023-2024-2") String semester,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            Map<String, Object> schedule = new HashMap<>();
            List<Map<String, Object>> classes = new ArrayList<>();
            
            classes.add(createClassData(1, 1, "数据结构", "教1-201", "计科2101班", 40));
            classes.add(createClassData(1, 3, "算法分析", "教2-303", "计科2102班", 35));
            classes.add(createClassData(2, 2, "操作系统", "教1-305", "计科2103班", 42));
            classes.add(createClassData(3, 1, "数据结构", "教1-201", "计科2101班", 40));
            classes.add(createClassData(3, 4, "数据库系统", "教2-207", "计科2102班", 38));
            classes.add(createClassData(4, 2, "操作系统", "教1-305", "计科2103班", 42));
            classes.add(createClassData(4, 5, "计算机网络", "教3-101", "网络2101班", 30));
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

    @GetMapping("/message")
    public HashMap<String, Object> getMessages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            List<Map<String, Object>> messages = new ArrayList<>();
            
            Map<String, Object> msg1 = new HashMap<>();
            msg1.put("id", 1);
            msg1.put("title", "课程调整通知");
            msg1.put("content", "由于特殊原因，本周三的数据结构课程调整到周五上午");
            msg1.put("date", "2024-03-15");
            msg1.put("isRead", false);
            messages.add(msg1);
            
            Map<String, Object> msg2 = new HashMap<>();
            msg2.put("id", 2);
            msg2.put("title", "考试安排");
            msg2.put("content", "期中考试将于4月10日进行，请提前做好准备");
            msg2.put("date", "2024-03-14");
            msg2.put("isRead", true);
            messages.add(msg2);
            
            Map<String, Object> msg3 = new HashMap<>();
            msg3.put("id", 3);
            msg3.put("title", "教室变更");
            msg3.put("content", "操作系统课程教室从教1-305变更到教2-201");
            msg3.put("date", "2024-03-13");
            msg3.put("isRead", false);
            messages.add(msg3);
            
            Map<String, Object> data = new HashMap<>();
            data.put("messages", messages);
            data.put("total", messages.size());
            data.put("page", page);
            data.put("size", size);
            
            result.put("code", 200);
            result.put("message", "请求成功");
            result.put("data", data);
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

    @PostMapping("/messages")
    public HashMap<String, Object> sendMessage(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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

    @PostMapping("/messages/{messageId}/read")
    public HashMap<String, Object> markMessageRead(
            @PathVariable Integer messageId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
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

    private String getLetterGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}