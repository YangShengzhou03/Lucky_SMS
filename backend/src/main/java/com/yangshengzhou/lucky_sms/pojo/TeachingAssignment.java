package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教学安排实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("teaching_assignments")
public class TeachingAssignment extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer assignmentId;
    
    @TableField("course_id")
    private Integer courseId;
    
    @TableField("teacher_id")
    private Integer teacherId;
    
    @TableField("semester_id")
    private Integer semesterId;
    
    @TableField("class_time")
    private String classTime;
    
    private String classroom;
    
    @TableField("max_students")
    private Integer maxStudents;
    
    @TableField("current_students")
    private Integer currentStudents;
    
    @TableField("credit_hours")
    private Integer creditHours;
    
    @TableField("course_type_id")
    private Integer courseTypeId;
    
    @TableField("assessment_method_id")
    private Integer assessmentMethodId;
    
    @TableField("status_id")
    private Integer statusId;
    
    private String description;
    private String requirements;
    

    @TableField(exist = false)
    private Course course;
    
    @TableField(exist = false)
    private Teacher teacher;
    
    @TableField(exist = false)
    private Semester semester;
    
    @TableField(exist = false)
    private CourseType courseType;
    
    @TableField(exist = false)
    private AssessmentMethod assessmentMethod;
    
    @TableField(exist = false)
    private StatusType status;
}