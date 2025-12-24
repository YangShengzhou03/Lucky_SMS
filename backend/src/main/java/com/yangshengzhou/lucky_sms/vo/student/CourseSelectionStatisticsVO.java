package com.yangshengzhou.lucky_sms.vo.student;

import lombok.Data;

import java.math.BigDecimal;

@Data
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
}