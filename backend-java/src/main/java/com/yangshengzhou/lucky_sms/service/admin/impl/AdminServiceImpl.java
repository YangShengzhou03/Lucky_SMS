package com.yangshengzhou.lucky_sms.service.admin.impl;

import com.yangshengzhou.lucky_sms.service.admin.AdminService;
import com.yangshengzhou.lucky_sms.vo.admin.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public AdminHomeVO getHomeData(Integer adminId) {
        AdminHomeVO homeVO = new AdminHomeVO();
        homeVO.setAdminName("系统管理员");
        homeVO.setAdminId(adminId);
        homeVO.setTotalUsers(1250);
        homeVO.setTotalStudents(1000);
        homeVO.setTotalTeachers(200);
        homeVO.setTotalCourses(150);
        homeVO.setTotalDepartments(12);
        homeVO.setActiveUsers(890);
        homeVO.setPendingApprovals(15);

        List<Map<String, Object>> recentActivities = new ArrayList<>();
        recentActivities.add(createActivityMap("用户注册", "新用户张三注册成功", "2024-12-26 10:30", "success"));
        recentActivities.add(createActivityMap("课程申请", "李老师提交了新课程申请", "2024-12-26 09:15", "warning"));
        recentActivities.add(createActivityMap("系统更新", "系统版本更新至 v2.1.0", "2024-12-25 18:00", "info"));
        recentActivities.add(createActivityMap("成绩录入", "王老师完成了《高等数学》成绩录入", "2024-12-25 16:30", "success"));
        recentActivities.add(createActivityMap("选课异常", "发现3个选课异常记录", "2024-12-25 14:20", "error"));
        homeVO.setRecentActivities(recentActivities);

        List<Map<String, Object>> systemStats = new ArrayList<>();
        systemStats.add(createStatMap("今日登录", 256, "+12%", "up"));
        systemStats.add(createStatMap("新增用户", 15, "+5%", "up"));
        systemStats.add(createStatMap("选课申请", 45, "-8%", "down"));
        systemStats.add(createStatMap("系统负载", 45, "正常", "stable"));
        homeVO.setSystemStats(systemStats);

        return homeVO;
    }

    @Override
    public UserManagementVO getUserList(Integer page, Integer size, String keyword, String role) {
        UserManagementVO vo = new UserManagementVO();
        List<UserManagementVO.UserInfo> users = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            UserManagementVO.UserInfo user = new UserManagementVO.UserInfo();
            user.setId(i);
            user.setUsername("user" + i);
            user.setRealName("用户" + i);
            user.setRole(i % 3 == 0 ? "TEACHER" : "STUDENT");
            user.setDepartment(i % 4 == 0 ? "计算机学院" : i % 4 == 1 ? "数学学院" : i % 4 == 2 ? "物理学院" : "化学学院");
            user.setEmail("user" + i + "@example.com");
            user.setPhone("1380013800" + i);
            user.setStatus(i % 10 == 0 ? "DISABLED" : "ACTIVE");
            user.setCreateTime("2024-01-" + String.format("%02d", i % 28 + 1));
            users.add(user);
        }

        List<UserManagementVO.UserInfo> filteredUsers = users;
        if (keyword != null && !keyword.isEmpty()) {
            filteredUsers = users.stream()
                    .filter(u -> u.getUsername().contains(keyword) || u.getRealName().contains(keyword))
                    .toList();
        }
        if (role != null && !role.isEmpty()) {
            filteredUsers = filteredUsers.stream()
                    .filter(u -> u.getRole().equals(role))
                    .toList();
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, filteredUsers.size());
        vo.setRecords(filteredUsers.subList(start, end));
        vo.setTotal((long) filteredUsers.size());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) Math.ceil((double) filteredUsers.size() / size));

        return vo;
    }

    @Override
    public CourseManagementVO getCourseList(Integer page, Integer size, String keyword, String department) {
        CourseManagementVO vo = new CourseManagementVO();
        List<CourseManagementVO.CourseInfo> courses = new ArrayList<>();

        String[] courseNames = {"高等数学", "线性代数", "概率论", "大学物理", "程序设计", "数据结构", "操作系统", "计算机网络", "数据库原理", "软件工程"};
        String[] courseTypes = {"必修", "选修", "专业核心", "通识教育"};
        String[] departments = {"计算机学院", "数学学院", "物理学院", "化学学院"};

        for (int i = 1; i <= 50; i++) {
            CourseManagementVO.CourseInfo course = new CourseManagementVO.CourseInfo();
            course.setId(i);
            course.setCourseCode("CS" + String.format("%04d", i));
            course.setCourseName(courseNames[i % courseNames.length] + (i / 10 + 1));
            course.setCourseType(courseTypes[i % courseTypes.length]);
            course.setDepartment(departments[i % departments.length]);
            course.setTeacherName("教师" + (i % 20 + 1));
            course.setCredits(2 + i % 4);
            course.setHours(32 + (i % 4) * 16);
            course.setSemester("2024-2025学年" + ((i % 2 == 0) ? "第一学期" : "第二学期"));
            course.setEnrolledCount(20 + i * 3);
            course.setMaxCapacity(60 + i * 2);
            course.setStatus(i % 10 == 0 ? "CLOSED" : "OPEN");
            courses.add(course);
        }

        List<CourseManagementVO.CourseInfo> filteredCourses = courses;
        if (keyword != null && !keyword.isEmpty()) {
            filteredCourses = courses.stream()
                    .filter(c -> c.getCourseName().contains(keyword) || c.getCourseCode().contains(keyword))
                    .toList();
        }
        if (department != null && !department.isEmpty()) {
            filteredCourses = filteredCourses.stream()
                    .filter(c -> c.getDepartment().equals(department))
                    .toList();
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, filteredCourses.size());
        vo.setRecords(filteredCourses.subList(start, end));
        vo.setTotal((long) filteredCourses.size());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) Math.ceil((double) filteredCourses.size() / size));

        return vo;
    }

    @Override
    public GradeManagementVO getGradeList(Integer page, Integer size, String keyword, String semester) {
        GradeManagementVO vo = new GradeManagementVO();
        List<GradeManagementVO.GradeInfo> grades = new ArrayList<>();

        String[] courseNames = {"高等数学", "线性代数", "概率论", "大学物理", "程序设计", "数据结构", "操作系统", "计算机网络"};
        String[] semesters = {"2024-2025学年第一学期", "2024-2025学年第二学期", "2023-2024学年第一学期", "2023-2024学年第二学期"};
        String[] statuses = {"PUBLISHED", "DRAFT", "REVIEW"};

        for (int i = 1; i <= 50; i++) {
            GradeManagementVO.GradeInfo grade = new GradeManagementVO.GradeInfo();
            grade.setId(i);
            grade.setStudentId("2021000" + (i % 50 + 1));
            grade.setStudentName("学生" + (i % 50 + 1));
            grade.setClassName("计科210" + (i % 3 + 1) + "班");
            grade.setCourseCode("CS" + String.format("%04d", i % 8 + 1));
            grade.setCourseName(courseNames[i % courseNames.length]);
            grade.setTeacherName("教师" + (i % 20 + 1));
            grade.setSemester(semesters[i % semesters.length]);
            double score = 60 + Math.random() * 40;
            grade.setScore(Math.round(score * 10) / 10.0);
            grade.setGrade(grade.getScore() >= 90 ? "A" : grade.getScore() >= 80 ? "B" : grade.getScore() >= 70 ? "C" : grade.getScore() >= 60 ? "D" : "F");
            grade.setStatus(statuses[i % statuses.length]);
            grades.add(grade);
        }

        List<GradeManagementVO.GradeInfo> filteredGrades = grades;
        if (keyword != null && !keyword.isEmpty()) {
            filteredGrades = grades.stream()
                    .filter(g -> g.getStudentName().contains(keyword) || g.getCourseName().contains(keyword))
                    .toList();
        }
        if (semester != null && !semester.isEmpty()) {
            filteredGrades = filteredGrades.stream()
                    .filter(g -> g.getSemester().equals(semester))
                    .toList();
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, filteredGrades.size());
        vo.setRecords(filteredGrades.subList(start, end));
        vo.setTotal((long) filteredGrades.size());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) Math.ceil((double) filteredGrades.size() / size));

        return vo;
    }

    @Override
    public DepartmentManagementVO getDepartmentList(Integer page, Integer size, String keyword) {
        DepartmentManagementVO vo = new DepartmentManagementVO();
        List<DepartmentManagementVO.DepartmentInfo> departments = new ArrayList<>();

        String[] deptNames = {"计算机学院", "数学学院", "物理学院", "化学学院", "生物学院", "外语学院", "经济学院", "管理学院", "法学院", "艺术学院", "体育学院", "工程学院"};

        for (int i = 1; i <= 12; i++) {
            DepartmentManagementVO.DepartmentInfo dept = new DepartmentManagementVO.DepartmentInfo();
            dept.setId(i);
            dept.setDeptCode("DEPT" + String.format("%02d", i));
            dept.setDeptName(deptNames[i - 1]);
            dept.setDeanName("院长" + i);
            dept.setPhone("010-8888" + String.format("%04d", i));
            dept.setEmail(dept.getDeptCode().toLowerCase() + "@university.edu.cn");
            dept.setTeacherCount(10 + i * 5);
            dept.setStudentCount(50 + i * 20);
            dept.setStatus(i % 10 == 0 ? "DISABLED" : "ACTIVE");
            departments.add(dept);
        }

        List<DepartmentManagementVO.DepartmentInfo> filteredDepts = departments;
        if (keyword != null && !keyword.isEmpty()) {
            filteredDepts = departments.stream()
                    .filter(d -> d.getDeptName().contains(keyword) || d.getDeptCode().contains(keyword))
                    .toList();
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, filteredDepts.size());
        vo.setRecords(filteredDepts.subList(start, end));
        vo.setTotal((long) filteredDepts.size());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) Math.ceil((double) filteredDepts.size() / size));

        return vo;
    }

    @Override
    public SystemSettingsVO getSystemSettings() {
        SystemSettingsVO settings = new SystemSettingsVO();
        settings.setSystemName("Lucky SMS 学生管理系统");
        settings.setSemester("2024-2025学年第一学期");
        settings.setAcademicYear("2024-2025");
        settings.setRegistrationStatus("OPEN");
        settings.setCourseSelectionStatus("OPEN");
        settings.setMaxCoursesPerStudent(8);
        settings.setMaxStudentsPerCourse(100);

        List<Map<String, Object>> notificationSettings = new ArrayList<>();
        notificationSettings.add(createNotificationSettingMap("邮件通知", true));
        notificationSettings.add(createNotificationSettingMap("短信通知", false));
        notificationSettings.add(createNotificationSettingMap("系统消息", true));
        settings.setNotificationSettings(notificationSettings);

        List<Map<String, Object>> securitySettings = new ArrayList<>();
        securitySettings.add(createSecuritySettingMap("密码强度", "高"));
        securitySettings.add(createSecuritySettingMap("登录失败锁定", "5次"));
        securitySettings.add(createSecuritySettingMap("会话超时", "30分钟"));
        settings.setSecuritySettings(securitySettings);

        return settings;
    }

    @Override
    public boolean updateSystemSettings(SystemSettingsVO settings) {
        return true;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return true;
    }

    @Override
    public boolean updateUserStatus(Integer userId, String status) {
        return true;
    }

    @Override
    public boolean deleteCourse(Integer courseId) {
        return true;
    }

    @Override
    public boolean updateCourseStatus(Integer courseId, String status) {
        return true;
    }

    @Override
    public boolean deleteDepartment(Integer deptId) {
        return true;
    }

    @Override
    public boolean updateDepartmentStatus(Integer deptId, String status) {
        return true;
    }

    private Map<String, Object> createActivityMap(String type, String description, String time, String status) {
        Map<String, Object> activity = new HashMap<>();
        activity.put("type", type);
        activity.put("description", description);
        activity.put("time", time);
        activity.put("status", status);
        return activity;
    }

    private Map<String, Object> createStatMap(String label, Integer value, String change, String trend) {
        Map<String, Object> stat = new HashMap<>();
        stat.put("label", label);
        stat.put("value", value);
        stat.put("change", change);
        stat.put("trend", trend);
        return stat;
    }

    private Map<String, Object> createNotificationSettingMap(String name, Boolean enabled) {
        Map<String, Object> setting = new HashMap<>();
        setting.put("name", name);
        setting.put("enabled", enabled);
        return setting;
    }

    private Map<String, Object> createSecuritySettingMap(String name, String value) {
        Map<String, Object> setting = new HashMap<>();
        setting.put("name", name);
        setting.put("value", value);
        return setting;
    }
}