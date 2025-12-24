package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程成绩实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_grades")
public class CourseGrade extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer gradeId;
    
    @TableField("student_id")
    private Integer studentId;
    
    @TableField("assignment_id")
    private Integer assignmentId;
    
    @TableField("usual_score")
    private BigDecimal usualScore;
    
    @TableField("midterm_score")
    private BigDecimal midtermScore;
    
    @TableField("final_score")
    private BigDecimal finalScore;
    
    @TableField("total_score")
    private BigDecimal totalScore;
    
    @TableField("gpa_grade")
    private BigDecimal gpaGrade;
    
    @TableField("grade_level_id")
    private Integer gradeLevelId;
    
    @TableField("make_up_score")
    private BigDecimal makeUpScore;
    
    @TableField("retake_score")
    private BigDecimal retakeScore;
    
    @TableField("review_status_id")
    private Integer reviewStatusId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("review_time")
    private LocalDateTime reviewTime;
    
    @TableField("reviewer_id")
    private Integer reviewerId;
    
    @TableField("review_comment")
    private String reviewComment;
    
    @TableField("status_id")
    private Integer statusId;
    

    @TableField(exist = false)
    private Student student;
    
    @TableField(exist = false)
    private TeachingAssignment assignment;
    
    @TableField(exist = false)
    private GradeLevel gradeLevel;
    
    @TableField(exist = false)
    private ReviewStatus reviewStatus;
    
    @TableField(exist = false)
    private User reviewer;
    
    @TableField(exist = false)
    private StatusType status;
}