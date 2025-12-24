package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("students")
public class Student extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer studentId;
    
    @TableField("student_no")
    private String studentNo;
    
    @TableField("user_id")
    private Integer userId;
    
    @TableField("department_id")
    private Integer departmentId;
    
    @TableField("major_id")
    private Integer majorId;
    
    @TableField("class_id")
    private Integer classId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("enrollment_date")
    private LocalDate enrollmentDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("graduation_date")
    private LocalDate graduationDate;
    
    @TableField("education_years")
    private Integer educationYears;
    
    @TableField("education_level_id")
    private Integer educationLevelId;
    
    @TableField("status_id")
    private Integer statusId;
    
    @TableField("emergency_contact")
    private String emergencyContact;
    
    @TableField("emergency_phone")
    private String emergencyPhone;
    
    @TableField("parent_name")
    private String parentName;
    
    @TableField("parent_phone")
    private String parentPhone;
    
    @TableField("home_address")
    private String homeAddress;
    
    @TableField("high_school")
    private String highSchool;
    
    @TableField("entrance_score")
    private BigDecimal entranceScore;
    
    @TableField("scholarship_info")
    private String scholarshipInfo;
    
    // 关联查询字段
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private Major major;
    
    @TableField(exist = false)
    private ClassInfo classInfo;
    
    @TableField(exist = false)
    private StatusType status;
}