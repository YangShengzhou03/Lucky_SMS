package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学期实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("semesters")
public class Semester extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer semesterId;
    
    @TableField("semester_name")
    private String semesterName;
    
    @TableField("academic_year")
    private String academicYear;
    
    @TableField("semester_type_id")
    private Integer semesterTypeId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("start_date")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("end_date")
    private LocalDate endDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("course_selection_start")
    private LocalDateTime courseSelectionStart;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("course_selection_end")
    private LocalDateTime courseSelectionEnd;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("grade_entry_start")
    private LocalDateTime gradeEntryStart;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("grade_entry_end")
    private LocalDateTime gradeEntryEnd;
    
    @TableField("status_id")
    private Integer statusId;
    

    @TableField(exist = false)
    private SemesterType semesterType;
    
    @TableField(exist = false)
    private StatusType status;
}