package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程选择实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_selections")
public class CourseSelection extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer selectionId;
    
    @TableField("student_id")
    private Integer studentId;
    
    @TableField("assignment_id")
    private Integer assignmentId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("selection_time")
    private LocalDateTime selectionTime;
    
    @TableField("selection_type_id")
    private Integer selectionTypeId;
    
    private Integer priority;
    
    @TableField("status_id")
    private Integer statusId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("drop_time")
    private LocalDateTime dropTime;
    
    @TableField("drop_reason")
    private String dropReason;
    
    @TableField("final_grade")
    private BigDecimal finalGrade;
    
    @TableField("attendance_rate")
    private BigDecimal attendanceRate;
    
    @TableField("evaluation_score")
    private BigDecimal evaluationScore;
    
    // 关联查询字段
    @TableField(exist = false)
    private Student student;
    
    @TableField(exist = false)
    private TeachingAssignment assignment;
    
    @TableField(exist = false)
    private SelectionType selectionType;
    
    @TableField(exist = false)
    private StatusType status;
}