package com.yangshengzhou.lucky_sms.vo.student;

import java.math.BigDecimal;

public class CourseSelectionStatisticsVO {
    
    private Integer studentId;
    private Integer semesterId;
    private String semesterName;
    
    private Integer totalCourses;
    private Integer selectedCourses;
    private Integer completedCourses;
    private Integer compulsoryCourses;
    private Integer electiveCourses;
    
    private BigDecimal totalCredits;
    private BigDecimal selectedCredits;
    private BigDecimal completedCredits;
    private BigDecimal gpa;
    
    private Integer timeConflictCount;
    private Integer creditLimit;
    private Integer courseLimit;
    
    private Boolean canSelectMore;
    private String restrictionMessage;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public Integer getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Integer totalCourses) {
        this.totalCourses = totalCourses;
    }

    public Integer getSelectedCourses() {
        return selectedCourses;
    }

    public void setSelectedCourses(Integer selectedCourses) {
        this.selectedCourses = selectedCourses;
    }

    public Integer getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(Integer completedCourses) {
        this.completedCourses = completedCourses;
    }

    public Integer getCompulsoryCourses() {
        return compulsoryCourses;
    }

    public void setCompulsoryCourses(Integer compulsoryCourses) {
        this.compulsoryCourses = compulsoryCourses;
    }

    public Integer getElectiveCourses() {
        return electiveCourses;
    }

    public void setElectiveCourses(Integer electiveCourses) {
        this.electiveCourses = electiveCourses;
    }

    public BigDecimal getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits) {
        this.totalCredits = totalCredits;
    }

    public BigDecimal getSelectedCredits() {
        return selectedCredits;
    }

    public void setSelectedCredits(BigDecimal selectedCredits) {
        this.selectedCredits = selectedCredits;
    }

    public BigDecimal getCompletedCredits() {
        return completedCredits;
    }

    public void setCompletedCredits(BigDecimal completedCredits) {
        this.completedCredits = completedCredits;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public Integer getTimeConflictCount() {
        return timeConflictCount;
    }

    public void setTimeConflictCount(Integer timeConflictCount) {
        this.timeConflictCount = timeConflictCount;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getCourseLimit() {
        return courseLimit;
    }

    public void setCourseLimit(Integer courseLimit) {
        this.courseLimit = courseLimit;
    }

    public Boolean getCanSelectMore() {
        return canSelectMore;
    }

    public void setCanSelectMore(Boolean canSelectMore) {
        this.canSelectMore = canSelectMore;
    }

    public String getRestrictionMessage() {
        return restrictionMessage;
    }

    public void setRestrictionMessage(String restrictionMessage) {
        this.restrictionMessage = restrictionMessage;
    }
}