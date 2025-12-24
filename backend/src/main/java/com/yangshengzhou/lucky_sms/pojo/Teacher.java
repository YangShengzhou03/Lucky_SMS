package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("teachers")
public class Teacher extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer teacherId;
    
    @TableField("teacher_no")
    private String teacherNo;
    
    @TableField("user_id")
    private Integer userId;
    
    @TableField("department_id")
    private Integer departmentId;
    
    @TableField("title_id")
    private Integer titleId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("hire_date")
    private LocalDate hireDate;
    
    @TableField("office_location")
    private String officeLocation;
    
    private String phone;
    private String email;
    
    @TableField("research_direction")
    private String researchDirection;
    
    @TableField("education_background")
    private String educationBackground;
    
    @TableField("work_experience")
    private String workExperience;
    
    @TableField("status_id")
    private Integer statusId;
    
    // 关联查询字段
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private Title title;
    
    @TableField(exist = false)
    private StatusType status;
}