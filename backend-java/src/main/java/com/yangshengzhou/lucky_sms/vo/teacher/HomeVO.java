package com.yangshengzhou.lucky_sms.vo.teacher;

import java.util.List;
import java.util.Map;

public class HomeVO {
    private String teacherName;
    private Integer teacherId;
    private Integer totalStudents;
    private Integer totalCourses;
    private Integer thisSemesterCourses;
    private Integer recentMessages;
    private Integer pendingTasks;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Integer getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Integer totalCourses) {
        this.totalCourses = totalCourses;
    }

    public Integer getThisSemesterCourses() {
        return thisSemesterCourses;
    }

    public void setThisSemesterCourses(Integer thisSemesterCourses) {
        this.thisSemesterCourses = thisSemesterCourses;
    }

    public Integer getRecentMessages() {
        return recentMessages;
    }

    public void setRecentMessages(Integer recentMessages) {
        this.recentMessages = recentMessages;
    }

    public Integer getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(Integer pendingTasks) {
        this.pendingTasks = pendingTasks;
    }
}
