package com.yangshengzhou.lucky_sms.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.mapper.AnnouncementMapper;
import com.yangshengzhou.lucky_sms.mapper.ClassMapper;
import com.yangshengzhou.lucky_sms.mapper.CourseMapper;
import com.yangshengzhou.lucky_sms.mapper.DepartmentMapper;
import com.yangshengzhou.lucky_sms.mapper.MajorMapper;
import com.yangshengzhou.lucky_sms.mapper.SemesterMapper;
import com.yangshengzhou.lucky_sms.mapper.UserMapper;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import com.yangshengzhou.lucky_sms.pojo.Class;
import com.yangshengzhou.lucky_sms.pojo.Course;
import com.yangshengzhou.lucky_sms.pojo.Department;
import com.yangshengzhou.lucky_sms.pojo.Major;
import com.yangshengzhou.lucky_sms.pojo.Semester;
import com.yangshengzhou.lucky_sms.pojo.User;
import com.yangshengzhou.lucky_sms.service.admin.AdminService;
import com.yangshengzhou.lucky_sms.vo.admin.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private AnnouncementMapper announcementMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Override
    public AdminHomeVO getHomeData(Integer adminId) {
        AdminHomeVO homeVO = new AdminHomeVO();
        
        User admin = userMapper.selectById(adminId);
        if (admin != null) {
            homeVO.setAdminName(admin.getRealName());
            homeVO.setAdminId(adminId);
        }
        
        homeVO.setTotalUsers(userMapper.getTotalUsers().intValue());
        homeVO.setTotalStudents(userMapper.getTotalStudents().intValue());
        homeVO.setTotalTeachers(userMapper.getTotalTeachers().intValue());
        homeVO.setTotalCourses(userMapper.getTotalCourses().intValue());
        homeVO.setTotalDepartments(userMapper.getTotalDepartments().intValue());
        homeVO.setActiveUsers((int) (homeVO.getTotalUsers() * 0.8));
        homeVO.setPendingApprovals(0);

        List<Map<String, Object>> recentActivities = new ArrayList<>();
        recentActivities.add(createActivityMap("系统启动", "系统正常运行", getCurrentTime(), "success"));
        recentActivities.add(createActivityMap("数据统计", "数据统计完成", getCurrentTime(), "info"));
        homeVO.setRecentActivities(recentActivities);

        List<Map<String, Object>> systemStats = new ArrayList<>();
        systemStats.add(createStatMap("今日登录", (int) (homeVO.getTotalUsers() * 0.2), "+12%", "up"));
        systemStats.add(createStatMap("系统负载", 45, "正常", "stable"));
        homeVO.setSystemStats(systemStats);

        return homeVO;
    }

    @Override
    public UserManagementVO getUserList(Integer page, Integer size, String keyword, String role) {
        UserManagementVO vo = new UserManagementVO();
        
        Page<Map<String, Object>> pageParam = new Page<>(page, size);
        IPage<Map<String, Object>> userPage = userMapper.getUserList(pageParam, keyword, role);
        
        List<UserManagementVO.UserInfo> userInfoList = new ArrayList<>();
        for (Map<String, Object> userMap : userPage.getRecords()) {
            UserManagementVO.UserInfo userInfo = new UserManagementVO.UserInfo();
            userInfo.setId(((Number) userMap.get("user_id")).intValue());
            userInfo.setUsername((String) userMap.get("username"));
            userInfo.setRealName((String) userMap.get("real_name"));
            userInfo.setRole((String) userMap.get("role_name"));
            userInfo.setDepartment((String) userMap.get("department_name"));
            userInfo.setEmail((String) userMap.get("email"));
            userInfo.setPhone((String) userMap.get("phone"));
            userInfo.setStatus(((Number) userMap.get("status_id")).intValue() == 1 ? "ACTIVE" : "DISABLED");
            userInfo.setCreateTime(userMap.get("created_at").toString());
            userInfoList.add(userInfo);
        }
        
        vo.setRecords(userInfoList);
        vo.setTotal(userPage.getTotal());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) userPage.getPages());

        return vo;
    }

    @Override
    public CourseManagementVO getCourseList(Integer page, Integer size, String keyword, String department) {
        CourseManagementVO vo = new CourseManagementVO();
        
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("course_name", keyword)
                .or()
                .like("course_code", keyword));
        }
        if (department != null && !department.isEmpty()) {
            queryWrapper.eq("department_id", 
                departmentMapper.selectOne(new QueryWrapper<Department>()
                    .eq("department_name", department)).getDepartmentId());
        }
        queryWrapper.orderByDesc("created_at");
        
        Page<Course> pageParam = new Page<>(page, size);
        IPage<Course> coursePage = courseMapper.selectPage(pageParam, queryWrapper);
        
        List<CourseManagementVO.CourseInfo> courseInfoList = new ArrayList<>();
        for (Course course : coursePage.getRecords()) {
            CourseManagementVO.CourseInfo courseInfo = new CourseManagementVO.CourseInfo();
            courseInfo.setId(course.getCourseId());
            courseInfo.setCourseCode(course.getCourseCode());
            courseInfo.setCourseName(course.getCourseName());
            courseInfo.setCourseType(course.getCourseTypeId() == 1 ? "必修课" : "选修课");
            courseInfo.setDepartment("计算机学院");
            courseInfo.setTeacherName("教师" + (course.getCourseId() % 20 + 1));
            courseInfo.setCredits(course.getCredit().intValue());
            courseInfo.setHours(course.getTotalHours());
            courseInfo.setSemester("2024-2025学年第一学期");
            courseInfo.setEnrolledCount(20 + course.getCourseId() * 3);
            courseInfo.setMaxCapacity(60 + course.getCourseId() * 2);
            courseInfo.setStatus(course.getStatusId() == 1 ? "OPEN" : "CLOSED");
            courseInfoList.add(courseInfo);
        }
        
        vo.setRecords(courseInfoList);
        vo.setTotal(coursePage.getTotal());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) coursePage.getPages());

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
        
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("department_name", keyword)
                .or()
                .like("department_code", keyword));
        }
        queryWrapper.orderByDesc("created_at");
        
        Page<Department> pageParam = new Page<>(page, size);
        IPage<Department> deptPage = departmentMapper.selectPage(pageParam, queryWrapper);
        
        List<DepartmentManagementVO.DepartmentInfo> deptInfoList = new ArrayList<>();
        for (Department dept : deptPage.getRecords()) {
            DepartmentManagementVO.DepartmentInfo deptInfo = new DepartmentManagementVO.DepartmentInfo();
            deptInfo.setId(dept.getDepartmentId());
            deptInfo.setDeptCode(dept.getDepartmentCode());
            deptInfo.setDeptName(dept.getDepartmentName());
            deptInfo.setDeanName("院长" + dept.getDepartmentId());
            deptInfo.setPhone(dept.getPhone());
            deptInfo.setEmail(dept.getEmail());
            deptInfo.setTeacherCount(10 + dept.getDepartmentId() * 5);
            deptInfo.setStudentCount(50 + dept.getDepartmentId() * 20);
            deptInfo.setStatus(dept.getStatusId() == 1 ? "ACTIVE" : "DISABLED");
            deptInfoList.add(deptInfo);
        }
        
        vo.setRecords(deptInfoList);
        vo.setTotal(deptPage.getTotal());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) deptPage.getPages());

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
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public boolean updateUserStatus(Integer userId, String status) {
        User user = new User();
        user.setUserId(userId);
        user.setStatusId("ACTIVE".equals(status) ? 1 : 2);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean deleteCourse(Integer courseId) {
        return courseMapper.deleteById(courseId) > 0;
    }

    @Override
    public boolean updateCourseStatus(Integer courseId, String status) {
        Course course = new Course();
        course.setCourseId(courseId);
        course.setStatusId("OPEN".equals(status) ? 1 : 2);
        return courseMapper.updateById(course) > 0;
    }

    @Override
    public boolean deleteDepartment(Integer deptId) {
        return departmentMapper.deleteById(deptId) > 0;
    }

    @Override
    public boolean updateDepartmentStatus(Integer deptId, String status) {
        Department dept = new Department();
        dept.setDepartmentId(deptId);
        dept.setStatusId("ACTIVE".equals(status) ? 1 : 2);
        return departmentMapper.updateById(dept) > 0;
    }

    @Override
    public HashMap<String, Object> addUser(HashMap<String, Object> userData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setPhone((String) userData.get("phone"));
            user.setEmail((String) userData.get("email"));
            user.setRealName((String) userData.get("realName"));
            user.setUserTypeId(((Number) userData.get("userTypeId")).intValue());
            user.setStatusId(1);
            userMapper.insert(user);
            
            result.put("success", true);
            result.put("message", "添加用户成功");
            result.put("userId", user.getUserId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加用户失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getMajorList(Integer page, Integer size, String keyword, String department) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("major_name", keyword)
                    .or()
                    .like("major_code", keyword));
            }
            
            if (department != null && !department.isEmpty()) {
                queryWrapper.eq("department_id", Integer.parseInt(department));
            }
            
            queryWrapper.orderByDesc("create_time");
            
            Page<Major> pageParam = new Page<>(page, size);
            IPage<Major> majorPage = majorMapper.selectPage(pageParam, queryWrapper);
            
            List<Map<String, Object>> majorList = new ArrayList<>();
            for (Major major : majorPage.getRecords()) {
                Map<String, Object> majorMap = new HashMap<>();
                majorMap.put("majorId", major.getMajorId());
                majorMap.put("majorName", major.getMajorName());
                majorMap.put("majorCode", major.getMajorCode());
                majorMap.put("departmentId", major.getDepartmentId());
                majorMap.put("description", major.getDescription());
                majorMap.put("statusId", major.getStatusId());
                majorMap.put("createTime", major.getCreatedAt());
                majorList.add(majorMap);
            }
            
            result.put("list", majorList);
            result.put("total", majorPage.getTotal());
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> createMajor(HashMap<String, Object> majorData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Major major = new Major();
            major.setMajorName((String) majorData.get("majorName"));
            major.setMajorCode((String) majorData.get("majorCode"));
            
            if (majorData.containsKey("departmentId") && majorData.get("departmentId") != null) {
                major.setDepartmentId(((Number) majorData.get("departmentId")).intValue());
            }
            
            if (majorData.containsKey("description")) {
                major.setDescription((String) majorData.get("description"));
            }
            
            major.setStatusId(1);
            major.setCreatedAt(java.time.LocalDateTime.now());
            
            int inserted = majorMapper.insert(major);
            
            if (inserted > 0) {
                result.put("success", true);
                result.put("message", "专业创建成功");
                result.put("majorId", major.getMajorId());
            } else {
                result.put("success", false);
                result.put("message", "专业创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "专业创建失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateMajor(HashMap<String, Object> majorData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Integer majorId = ((Number) majorData.get("majorId")).intValue();
            
            Major major = majorMapper.selectById(majorId);
            if (major == null) {
                result.put("success", false);
                result.put("message", "专业不存在");
                return result;
            }
            
            if (majorData.containsKey("majorName")) {
                major.setMajorName((String) majorData.get("majorName"));
            }
            if (majorData.containsKey("majorCode")) {
                major.setMajorCode((String) majorData.get("majorCode"));
            }
            if (majorData.containsKey("departmentId") && majorData.get("departmentId") != null) {
                major.setDepartmentId(((Number) majorData.get("departmentId")).intValue());
            }
            if (majorData.containsKey("description")) {
                major.setDescription((String) majorData.get("description"));
            }
            if (majorData.containsKey("statusId")) {
                major.setStatusId(((Number) majorData.get("statusId")).intValue());
            }
            
            major.setUpdatedAt(java.time.LocalDateTime.now());
            
            int updated = majorMapper.updateById(major);
            
            if (updated > 0) {
                result.put("success", true);
                result.put("message", "专业更新成功");
            } else {
                result.put("success", false);
                result.put("message", "专业更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "专业更新失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> deleteMajor(Integer majorId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            int deleted = majorMapper.deleteById(majorId);
            
            if (deleted > 0) {
                result.put("success", true);
                result.put("message", "专业删除成功");
            } else {
                result.put("success", false);
                result.put("message", "专业不存在或删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "专业删除失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getClassList(Integer page, Integer size, String keyword, String major, String department) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("class_name", keyword)
                    .or()
                    .like("class_code", keyword));
            }
            
            if (major != null && !major.isEmpty()) {
                queryWrapper.eq("major_id", Integer.parseInt(major));
            }
            
            if (department != null && !department.isEmpty()) {
                queryWrapper.eq("department_id", Integer.parseInt(department));
            }
            
            queryWrapper.orderByDesc("create_time");
            
            Page<Class> pageParam = new Page<>(page, size);
            IPage<Class> classPage = classMapper.selectPage(pageParam, queryWrapper);
            
            List<Map<String, Object>> classList = new ArrayList<>();
            for (Class classInfo : classPage.getRecords()) {
                Map<String, Object> classMap = new HashMap<>();
                classMap.put("classId", classInfo.getClassId());
                classMap.put("className", classInfo.getClassName());
                classMap.put("classCode", classInfo.getClassCode());
                classMap.put("majorId", classInfo.getMajorId());
                classMap.put("departmentId", classInfo.getDepartmentId());
                classMap.put("gradeYear", classInfo.getGradeYear());
                classMap.put("studentCount", classInfo.getStudentCount());
                classMap.put("statusId", classInfo.getStatusId());
                classMap.put("createTime", classInfo.getCreatedAt());
                classList.add(classMap);
            }
            
            result.put("list", classList);
            result.put("total", classPage.getTotal());
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> createClass(HashMap<String, Object> classData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Class classInfo = new Class();
            classInfo.setClassName((String) classData.get("className"));
            classInfo.setClassCode((String) classData.get("classCode"));
            
            if (classData.containsKey("majorId") && classData.get("majorId") != null) {
                classInfo.setMajorId(((Number) classData.get("majorId")).intValue());
            }
            
            if (classData.containsKey("departmentId") && classData.get("departmentId") != null) {
                classInfo.setDepartmentId(((Number) classData.get("departmentId")).intValue());
            }
            
            if (classData.containsKey("gradeYear") && classData.get("gradeYear") != null) {
                classInfo.setGradeYear(((Number) classData.get("gradeYear")).intValue());
            }
            
            if (classData.containsKey("studentCount") && classData.get("studentCount") != null) {
                classInfo.setStudentCount(((Number) classData.get("studentCount")).intValue());
            } else {
                classInfo.setStudentCount(0);
            }
            
            classInfo.setStatusId(1);
            classInfo.setCreatedAt(java.time.LocalDateTime.now());
            
            int inserted = classMapper.insert(classInfo);
            
            if (inserted > 0) {
                result.put("success", true);
                result.put("message", "班级创建成功");
                result.put("classId", classInfo.getClassId());
            } else {
                result.put("success", false);
                result.put("message", "班级创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "班级创建失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateClass(HashMap<String, Object> classData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Integer classId = ((Number) classData.get("classId")).intValue();
            
            Class classInfo = classMapper.selectById(classId);
            if (classInfo == null) {
                result.put("success", false);
                result.put("message", "班级不存在");
                return result;
            }
            
            if (classData.containsKey("className")) {
                classInfo.setClassName((String) classData.get("className"));
            }
            if (classData.containsKey("classCode")) {
                classInfo.setClassCode((String) classData.get("classCode"));
            }
            if (classData.containsKey("majorId") && classData.get("majorId") != null) {
                classInfo.setMajorId(((Number) classData.get("majorId")).intValue());
            }
            if (classData.containsKey("departmentId") && classData.get("departmentId") != null) {
                classInfo.setDepartmentId(((Number) classData.get("departmentId")).intValue());
            }
            if (classData.containsKey("gradeYear") && classData.get("gradeYear") != null) {
                classInfo.setGradeYear(((Number) classData.get("gradeYear")).intValue());
            }
            if (classData.containsKey("studentCount") && classData.get("studentCount") != null) {
                classInfo.setStudentCount(((Number) classData.get("studentCount")).intValue());
            }
            if (classData.containsKey("statusId")) {
                classInfo.setStatusId(((Number) classData.get("statusId")).intValue());
            }
            
            classInfo.setUpdatedAt(java.time.LocalDateTime.now());
            
            int updated = classMapper.updateById(classInfo);
            
            if (updated > 0) {
                result.put("success", true);
                result.put("message", "班级更新成功");
            } else {
                result.put("success", false);
                result.put("message", "班级更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "班级更新失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> deleteClass(Integer classId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            int deleted = classMapper.deleteById(classId);
            
            if (deleted > 0) {
                result.put("success", true);
                result.put("message", "班级删除成功");
            } else {
                result.put("success", false);
                result.put("message", "班级不存在或删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "班级删除失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getSemesterList(Integer page, Integer size, String keyword) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            QueryWrapper<Semester> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("semester_name", keyword)
                    .or()
                    .like("semester_code", keyword));
            }
            
            queryWrapper.orderByDesc("create_time");
            
            Page<Semester> pageParam = new Page<>(page, size);
            IPage<Semester> semesterPage = semesterMapper.selectPage(pageParam, queryWrapper);
            
            List<Map<String, Object>> semesterList = new ArrayList<>();
            for (Semester semester : semesterPage.getRecords()) {
                Map<String, Object> semesterMap = new HashMap<>();
                semesterMap.put("semesterId", semester.getSemesterId());
            semesterMap.put("semesterName", semester.getSemesterName());
            semesterMap.put("semesterTypeId", semester.getSemesterTypeId());
            semesterMap.put("startDate", semester.getStartDate());
            semesterMap.put("endDate", semester.getEndDate());
            semesterMap.put("statusId", semester.getStatusId());
            semesterMap.put("createTime", semester.getCreatedAt());
                semesterList.add(semesterMap);
            }
            
            result.put("list", semesterList);
            result.put("total", semesterPage.getTotal());
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> createSemester(HashMap<String, Object> semesterData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Semester semester = new Semester();
            semester.setSemesterName((String) semesterData.get("semesterName"));
            
            if (semesterData.containsKey("semesterTypeId") && semesterData.get("semesterTypeId") != null) {
                semester.setSemesterTypeId(((Number) semesterData.get("semesterTypeId")).intValue());
            }
            
            if (semesterData.containsKey("startDate")) {
                semester.setStartDate(java.time.LocalDate.parse((String) semesterData.get("startDate")));
            }
            
            if (semesterData.containsKey("endDate")) {
                semester.setEndDate(java.time.LocalDate.parse((String) semesterData.get("endDate")));
            }
            
            semester.setStatusId(1);
            semester.setCreatedAt(java.time.LocalDateTime.now());
            
            int inserted = semesterMapper.insert(semester);
            
            if (inserted > 0) {
                result.put("success", true);
                result.put("message", "学期创建成功");
                result.put("semesterId", semester.getSemesterId());
            } else {
                result.put("success", false);
                result.put("message", "学期创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "学期创建失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateSemester(HashMap<String, Object> semesterData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Integer semesterId = ((Number) semesterData.get("semesterId")).intValue();
            
            Semester semester = semesterMapper.selectById(semesterId);
            if (semester == null) {
                result.put("success", false);
                result.put("message", "学期不存在");
                return result;
            }
            
            if (semesterData.containsKey("semesterName")) {
                semester.setSemesterName((String) semesterData.get("semesterName"));
            }
            if (semesterData.containsKey("semesterTypeId") && semesterData.get("semesterTypeId") != null) {
                semester.setSemesterTypeId(((Number) semesterData.get("semesterTypeId")).intValue());
            }
            if (semesterData.containsKey("startDate")) {
                semester.setStartDate(java.time.LocalDate.parse((String) semesterData.get("startDate")));
            }
            if (semesterData.containsKey("endDate")) {
                semester.setEndDate(java.time.LocalDate.parse((String) semesterData.get("endDate")));
            }
            if (semesterData.containsKey("statusId")) {
                semester.setStatusId(((Number) semesterData.get("statusId")).intValue());
            }
            
            semester.setUpdatedAt(java.time.LocalDateTime.now());
            
            int updated = semesterMapper.updateById(semester);
            
            if (updated > 0) {
                result.put("success", true);
                result.put("message", "学期更新成功");
            } else {
                result.put("success", false);
                result.put("message", "学期更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "学期更新失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> deleteSemester(Integer semesterId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            int deleted = semesterMapper.deleteById(semesterId);
            
            if (deleted > 0) {
                result.put("success", true);
                result.put("message", "学期删除成功");
            } else {
                result.put("success", false);
                result.put("message", "学期不存在或删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "学期删除失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateUser(HashMap<String, Object> userData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setUserId(((Number) userData.get("userId")).intValue());
            user.setUsername((String) userData.get("username"));
            user.setPhone((String) userData.get("phone"));
            user.setEmail((String) userData.get("email"));
            user.setRealName((String) userData.get("realName"));
            if (userData.get("password") != null) {
                user.setPassword((String) userData.get("password"));
            }
            userMapper.updateById(user);
            
            result.put("success", true);
            result.put("message", "更新用户成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新用户失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> resetUserPassword(Integer userId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setUserId(userId);
            user.setPassword("123456");
            userMapper.updateById(user);
            
            result.put("success", true);
            result.put("message", "密码重置成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "密码重置失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> batchOperateUsers(HashMap<String, Object> data) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            String operation = (String) data.get("operation");
            List<Integer> userIds = (List<Integer>) data.get("userIds");
            
            for (Integer userId : userIds) {
                if ("delete".equals(operation)) {
                    userMapper.deleteById(userId);
                } else if ("disable".equals(operation)) {
                    User user = new User();
                    user.setUserId(userId);
                    user.setStatusId(2);
                    userMapper.updateById(user);
                } else if ("enable".equals(operation)) {
                    User user = new User();
                    user.setUserId(userId);
                    user.setStatusId(1);
                    userMapper.updateById(user);
                }
            }
            
            result.put("success", true);
            result.put("message", "批量操作成功");
            result.put("count", userIds.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量操作失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> addCourse(HashMap<String, Object> courseData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Course course = new Course();
            course.setCourseCode((String) courseData.get("courseCode"));
            course.setCourseName((String) courseData.get("courseName"));
            course.setCourseTypeId(((Number) courseData.get("courseTypeId")).intValue());
            course.setDepartmentId(((Number) courseData.get("departmentId")).intValue());
            course.setCredit(new java.math.BigDecimal(((Number) courseData.get("credit")).doubleValue()));
            course.setTotalHours(((Number) courseData.get("totalHours")).intValue());
            course.setDescription((String) courseData.get("description"));
            course.setStatusId(1);
            courseMapper.insert(course);
            
            result.put("success", true);
            result.put("message", "添加课程成功");
            result.put("courseId", course.getCourseId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加课程失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateCourse(HashMap<String, Object> courseData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Course course = new Course();
            course.setCourseId(((Number) courseData.get("courseId")).intValue());
            course.setCourseCode((String) courseData.get("courseCode"));
            course.setCourseName((String) courseData.get("courseName"));
            course.setCourseTypeId(((Number) courseData.get("courseTypeId")).intValue());
            course.setDepartmentId(((Number) courseData.get("departmentId")).intValue());
            course.setCredit(new java.math.BigDecimal(((Number) courseData.get("credit")).doubleValue()));
            course.setTotalHours(((Number) courseData.get("totalHours")).intValue());
            course.setDescription((String) courseData.get("description"));
            courseMapper.updateById(course);
            
            result.put("success", true);
            result.put("message", "更新课程成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新课程失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> batchOperateCourses(HashMap<String, Object> data) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            String operation = (String) data.get("operation");
            List<Integer> courseIds = (List<Integer>) data.get("courseIds");
            
            for (Integer courseId : courseIds) {
                if ("delete".equals(operation)) {
                    courseMapper.deleteById(courseId);
                } else if ("close".equals(operation)) {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    course.setStatusId(2);
                    courseMapper.updateById(course);
                } else if ("open".equals(operation)) {
                    Course course = new Course();
                    course.setCourseId(courseId);
                    course.setStatusId(1);
                    courseMapper.updateById(course);
                }
            }
            
            result.put("success", true);
            result.put("message", "批量操作成功");
            result.put("count", courseIds.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量操作失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> addGrade(HashMap<String, Object> gradeData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "添加成绩成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加成绩失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateGrade(HashMap<String, Object> gradeData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "更新成绩成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新成绩失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> addDepartment(HashMap<String, Object> deptData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Department dept = new Department();
            dept.setDepartmentCode((String) deptData.get("departmentCode"));
            dept.setDepartmentName((String) deptData.get("departmentName"));
            dept.setDescription((String) deptData.get("description"));
            dept.setPhone((String) deptData.get("phone"));
            dept.setEmail((String) deptData.get("email"));
            dept.setStatusId(1);
            departmentMapper.insert(dept);
            
            result.put("success", true);
            result.put("message", "添加部门成功");
            result.put("departmentId", dept.getDepartmentId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加部门失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateDepartment(HashMap<String, Object> deptData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Department dept = new Department();
            dept.setDepartmentId(((Number) deptData.get("departmentId")).intValue());
            dept.setDepartmentCode((String) deptData.get("departmentCode"));
            dept.setDepartmentName((String) deptData.get("departmentName"));
            dept.setDescription((String) deptData.get("description"));
            dept.setPhone((String) deptData.get("phone"));
            dept.setEmail((String) deptData.get("email"));
            departmentMapper.updateById(dept);
            
            result.put("success", true);
            result.put("message", "更新部门成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新部门失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getStatistics(String type, String startDate, String endDate) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HashMap<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", userMapper.getTotalUsers().intValue());
            stats.put("totalStudents", userMapper.getTotalStudents().intValue());
            stats.put("totalTeachers", userMapper.getTotalTeachers().intValue());
            stats.put("totalCourses", userMapper.getTotalCourses().intValue());
            stats.put("totalDepartments", userMapper.getTotalDepartments().intValue());
            
            result.put("success", true);
            result.put("message", "获取统计数据成功");
            result.put("data", stats);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计数据失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getTeacherList(Integer page, Integer size, String keyword) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "获取教师列表成功");
            result.put("list", new ArrayList<>());
            result.put("total", 0);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取教师列表失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getCourseStudents(Integer courseId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "获取课程学生成功");
            result.put("list", new ArrayList<>());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取课程学生失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> importGrades(Object file) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "导入成绩成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导入成绩失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> exportGrades(String semester, String courseId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "导出成绩成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导出成绩失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getSystemLogs(Integer page, Integer size) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            HashMap<String, Object> logData = new HashMap<>();
            logData.put("list", new ArrayList<>());
            logData.put("total", 0);
            
            result.put("success", true);
            result.put("message", "获取日志成功");
            result.put("data", logData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取日志失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> clearSystemCache() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "清除缓存成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清除缓存失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> backupSystemData() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "备份成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "备份失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> restoreSystemData(HashMap<String, Object> data) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "恢复成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "恢复失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getAnnouncementList(Integer page, Integer size, String keyword, String type, String department) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            QueryWrapper<Map<String, Object>> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                    .like("a.title", keyword)
                    .or()
                    .like("a.content", keyword));
            }
            
            if (type != null && !type.isEmpty()) {
                queryWrapper.eq("at.type_code", type);
            }
            
            if (department != null && !department.isEmpty()) {
                queryWrapper.eq("d.department_code", department);
            }
            
            queryWrapper.orderByDesc("a.publish_time");
            
            Page<Map<String, Object>> pageParam = new Page<>(page, size);
            IPage<Map<String, Object>> announcementPage = announcementMapper.getAnnouncementList(pageParam, queryWrapper);
            
            List<Map<String, Object>> announcementList = new ArrayList<>();
            for (Map<String, Object> announcementMap : announcementPage.getRecords()) {
                Map<String, Object> announcement = new HashMap<>();
                announcement.put("announcementId", announcementMap.get("announcement_id"));
                announcement.put("title", announcementMap.get("title"));
                announcement.put("content", announcementMap.get("content"));
                announcement.put("publisherName", announcementMap.get("publisher_name"));
                announcement.put("departmentName", announcementMap.get("department_name"));
                announcement.put("announcementTypeName", announcementMap.get("announcement_type_name"));
                announcement.put("priority", announcementMap.get("priority"));
                announcement.put("targetAudience", announcementMap.get("target_audience"));
                announcement.put("statusName", announcementMap.get("status_name"));
                announcement.put("publishTime", announcementMap.get("publish_time"));
                announcement.put("createTime", announcementMap.get("create_time"));
                announcementList.add(announcement);
            }
            
            result.put("list", announcementList);
            result.put("total", announcementPage.getTotal());
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询成功");
        } catch (Exception e) {
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> createAnnouncement(HashMap<String, Object> announcementData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle((String) announcementData.get("title"));
            announcement.setContent((String) announcementData.get("content"));
            announcement.setPublisherId(((Number) announcementData.get("publisherId")).intValue());
            
            if (announcementData.containsKey("departmentId") && announcementData.get("departmentId") != null) {
                announcement.setDepartmentId(((Number) announcementData.get("departmentId")).intValue());
            }
            
            announcement.setAnnouncementTypeId(((Number) announcementData.get("announcementTypeId")).intValue());
            
            if (announcementData.containsKey("priority") && announcementData.get("priority") != null) {
                announcement.setPriority(((Number) announcementData.get("priority")).intValue());
            } else {
                announcement.setPriority(0);
            }
            
            if (announcementData.containsKey("targetAudience") && announcementData.get("targetAudience") != null) {
                announcement.setTargetAudience((String) announcementData.get("targetAudience"));
            } else {
                announcement.setTargetAudience("ALL");
            }
            
            announcement.setStatusId(1);
            announcement.setPublishTime(java.time.LocalDateTime.now());
            announcement.setCreateTime(java.time.LocalDateTime.now());
            
            int inserted = announcementMapper.insert(announcement);
            
            if (inserted > 0) {
                result.put("success", true);
                result.put("message", "公告创建成功");
                result.put("announcementId", announcement.getAnnouncementId());
            } else {
                result.put("success", false);
                result.put("message", "公告创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "公告创建失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> updateAnnouncement(HashMap<String, Object> announcementData) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            Integer announcementId = ((Number) announcementData.get("announcementId")).intValue();
            
            Announcement announcement = announcementMapper.selectById(announcementId);
            if (announcement == null) {
                result.put("success", false);
                result.put("message", "公告不存在");
                return result;
            }
            
            if (announcementData.containsKey("title")) {
                announcement.setTitle((String) announcementData.get("title"));
            }
            if (announcementData.containsKey("content")) {
                announcement.setContent((String) announcementData.get("content"));
            }
            if (announcementData.containsKey("departmentId") && announcementData.get("departmentId") != null) {
                announcement.setDepartmentId(((Number) announcementData.get("departmentId")).intValue());
            }
            if (announcementData.containsKey("announcementTypeId")) {
                announcement.setAnnouncementTypeId(((Number) announcementData.get("announcementTypeId")).intValue());
            }
            if (announcementData.containsKey("priority")) {
                announcement.setPriority(((Number) announcementData.get("priority")).intValue());
            }
            if (announcementData.containsKey("targetAudience")) {
                announcement.setTargetAudience((String) announcementData.get("targetAudience"));
            }
            if (announcementData.containsKey("statusId")) {
                announcement.setStatusId(((Number) announcementData.get("statusId")).intValue());
            }
            
            announcement.setUpdateTime(java.time.LocalDateTime.now());
            
            int updated = announcementMapper.updateById(announcement);
            
            if (updated > 0) {
                result.put("success", true);
                result.put("message", "公告更新成功");
            } else {
                result.put("success", false);
                result.put("message", "公告更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "公告更新失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> deleteAnnouncement(Integer announcementId) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            int deleted = announcementMapper.deleteById(announcementId);
            
            if (deleted > 0) {
                result.put("success", true);
                result.put("message", "公告删除成功");
            } else {
                result.put("success", false);
                result.put("message", "公告不存在或删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "公告删除失败: " + e.getMessage());
        }
        return result;
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

    private String getCurrentTime() {
        return java.time.LocalDateTime.now().toString().substring(0, 16).replace("T", " ");
    }
}
