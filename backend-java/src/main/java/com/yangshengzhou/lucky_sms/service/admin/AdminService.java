package com.yangshengzhou.lucky_sms.service.admin;

import com.yangshengzhou.lucky_sms.vo.admin.*;
import java.util.HashMap;
import java.util.Map;

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

    HashMap<String, Object> addUser(HashMap<String, Object> userData);

    HashMap<String, Object> updateUser(HashMap<String, Object> userData);

    HashMap<String, Object> resetUserPassword(Integer userId);

    HashMap<String, Object> batchOperateUsers(HashMap<String, Object> data);

    HashMap<String, Object> addCourse(HashMap<String, Object> courseData);

    HashMap<String, Object> updateCourse(HashMap<String, Object> courseData);

    HashMap<String, Object> batchOperateCourses(HashMap<String, Object> data);

    HashMap<String, Object> addGrade(HashMap<String, Object> gradeData);

    HashMap<String, Object> updateGrade(HashMap<String, Object> gradeData);

    HashMap<String, Object> addDepartment(HashMap<String, Object> deptData);

    HashMap<String, Object> updateDepartment(HashMap<String, Object> deptData);

    HashMap<String, Object> getStatistics(String type, String startDate, String endDate);

    HashMap<String, Object> getTeacherList(Integer page, Integer size, String keyword);

    HashMap<String, Object> getCourseStudents(Integer courseId);

    HashMap<String, Object> importGrades(Object file);

    HashMap<String, Object> exportGrades(String semester, String courseId);

    HashMap<String, Object> getSystemLogs(Integer page, Integer size);

    HashMap<String, Object> clearSystemCache();

    HashMap<String, Object> backupSystemData();

    HashMap<String, Object> restoreSystemData(HashMap<String, Object> data);

    HashMap<String, Object> getAnnouncementList(Integer page, Integer size, String keyword, String type, String department);

    HashMap<String, Object> createAnnouncement(HashMap<String, Object> announcementData);

    HashMap<String, Object> updateAnnouncement(HashMap<String, Object> announcementData);

    HashMap<String, Object> deleteAnnouncement(Integer announcementId);

    HashMap<String, Object> getMajorList(Integer page, Integer size, String keyword, String department);

    HashMap<String, Object> createMajor(HashMap<String, Object> majorData);

    HashMap<String, Object> updateMajor(HashMap<String, Object> majorData);

    HashMap<String, Object> deleteMajor(Integer majorId);

    HashMap<String, Object> getClassList(Integer page, Integer size, String keyword, String major, String department);

    HashMap<String, Object> createClass(HashMap<String, Object> classData);

    HashMap<String, Object> updateClass(HashMap<String, Object> classData);

    HashMap<String, Object> deleteClass(Integer classId);

    HashMap<String, Object> getSemesterList(Integer page, Integer size, String keyword);

    HashMap<String, Object> createSemester(HashMap<String, Object> semesterData);

    HashMap<String, Object> updateSemester(HashMap<String, Object> semesterData);

    HashMap<String, Object> deleteSemester(Integer semesterId);
}