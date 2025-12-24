package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 班级实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("class_info")
public class ClassInfo extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer classId;
    
    @TableField("class_code")
    private String classCode;
    
    @TableField("class_name")
    private String className;
    
    @TableField("major_id")
    private Integer majorId;
    
    @TableField("department_id")
    private Integer departmentId;
    
    @TableField("grade_level")
    private Integer gradeLevel;
    
    @TableField("class_number")
    private Integer classNumber;
    
    @TableField("academic_year")
    private String academicYear;
    
    @TableField("head_teacher_user_id")
    private Integer headTeacherUserId;
    
    @TableField("counselor_user_id")
    private Integer counselorUserId;
    
    @TableField("student_count")
    private Integer studentCount;
    
    @TableField("max_students")
    private Integer maxStudents;
    
    @TableField("status_id")
    private Integer statusId;
    

    @TableField(exist = false)
    private Major major;
    
    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private User headTeacher;
    
    @TableField(exist = false)
    private User counselor;
    
    @TableField(exist = false)
    private StatusType status;
}