package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 课程实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("courses")
public class Course extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer courseId;
    
    @TableField("course_code")
    private String courseCode;
    
    @TableField("course_name")
    private String courseName;
    
    @TableField("course_type_id")
    private Integer courseTypeId;
    
    private BigDecimal credit;
    
    @TableField("total_hours")
    private Integer totalHours;
    
    @TableField("theory_hours")
    private Integer theoryHours;
    
    @TableField("practice_hours")
    private Integer practiceHours;
    
    @TableField("department_id")
    private Integer departmentId;
    
    @TableField("major_id")
    private Integer majorId;
    
    private String description;
    private String syllabus;
    private String textbook;
    private String prerequisites;
    
    @TableField("is_compulsory")
    private Boolean isCompulsory;
    
    @TableField("status_id")
    private Integer statusId;
    
    // 关联查询字段
    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private Major major;
    
    @TableField(exist = false)
    private CourseType courseType;
    
    @TableField(exist = false)
    private StatusType status;
}