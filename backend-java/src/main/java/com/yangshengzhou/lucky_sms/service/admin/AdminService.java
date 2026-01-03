package com.yangshengzhou.lucky_sms.service.admin;

import com.yangshengzhou.lucky_sms.vo.admin.*;

public interface AdminService {
    AdminHomeVO getHomeData(Integer adminId);

    UserManagementVO getUserList(Integer page, Integer size, String keyword, String role);

    CourseManagementVO getCourseList(Integer page, Integer size, String keyword, String department);

    GradeManagementVO getGradeList(Integer page, Integer size, String keyword, String semester);

    DepartmentManagementVO getDepartmentList(Integer page, Integer size, String keyword);

    SystemSettingsVO getSystemSettings();

    boolean updateSystemSettings(SystemSettingsVO settings);

    boolean deleteUser(Integer userId);

    boolean updateUserStatus(Integer userId, String status);

    boolean deleteCourse(Integer courseId);

    boolean updateCourseStatus(Integer courseId, String status);

    boolean deleteDepartment(Integer deptId);

    boolean updateDepartmentStatus(Integer deptId, String status);
}