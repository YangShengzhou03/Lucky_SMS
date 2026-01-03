package com.yangshengzhou.lucky_sms.vo.admin;

import java.util.List;
import java.util.Map;

public class SystemSettingsVO {
    private String systemName;
    private String semester;
    private String academicYear;
    private String registrationStatus;
    private String courseSelectionStatus;
    private Integer maxCoursesPerStudent;
    private Integer maxStudentsPerCourse;
    private List<Map<String, Object>> notificationSettings;
    private List<Map<String, Object>> securitySettings;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getCourseSelectionStatus() {
        return courseSelectionStatus;
    }

    public void setCourseSelectionStatus(String courseSelectionStatus) {
        this.courseSelectionStatus = courseSelectionStatus;
    }

    public Integer getMaxCoursesPerStudent() {
        return maxCoursesPerStudent;
    }

    public void setMaxCoursesPerStudent(Integer maxCoursesPerStudent) {
        this.maxCoursesPerStudent = maxCoursesPerStudent;
    }

    public Integer getMaxStudentsPerCourse() {
        return maxStudentsPerCourse;
    }

    public void setMaxStudentsPerCourse(Integer maxStudentsPerCourse) {
        this.maxStudentsPerCourse = maxStudentsPerCourse;
    }

    public List<Map<String, Object>> getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(List<Map<String, Object>> notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public List<Map<String, Object>> getSecuritySettings() {
        return securitySettings;
    }

    public void setSecuritySettings(List<Map<String, Object>> securitySettings) {
        this.securitySettings = securitySettings;
    }
}