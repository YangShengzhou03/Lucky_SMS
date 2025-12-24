package com.yangshengzhou.lucky_sms.vo.student;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 选课统计信息VO
 */
@Data
public class CourseSelectionStatisticsVO {
    
    private Integer studentId;
    private Integer semesterId;
    private String semesterName;
    
    private Integer totalCourses; // 总课程数
    private Integer selectedCourses; // 已选课程数
    private Integer completedCourses; // 已完成课程数
    private Integer compulsoryCourses; // 必修课数
    private Integer electiveCourses; // 选修课数
    
    private BigDecimal totalCredits; // 总学分
    private BigDecimal selectedCredits; // 已选学分
    private BigDecimal completedCredits; // 已完成学分
    private BigDecimal gpa; // 平均绩点
    
    private Integer timeConflictCount; // 时间冲突数
    private Integer creditLimit; // 学分上限
    private Integer courseLimit; // 课程数上限
    
    private Boolean canSelectMore; // 是否还能选课
    private String restrictionMessage; // 限制信息
}