package com.yangshengzhou.lucky_sms.vo.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseSelectionVO {
    
    private Integer selectionId;
    private Integer studentId;
    private Integer assignmentId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime selectionTime;
    
    private Integer selectionTypeId;
    private String selectionTypeName;
    private Integer priority;
    private Integer statusId;
    private String statusName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dropTime;
    
    private String dropReason;
    private BigDecimal finalGrade;
    private BigDecimal attendanceRate;
    private BigDecimal evaluationScore;
    
    private Integer courseId;
    private String courseCode;
    private String courseName;
    private BigDecimal credit;
    private String courseDescription;
    
    private Integer teacherId;
    private String teacherNo;
    private String teacherName;
    private String teacherPhone;
    private String teacherEmail;
    
    private Integer semesterId;
    private String semesterName;
    private String academicYear;
    
    private String classTime;
    private String classroom;
    private Integer maxStudents;
    private Integer currentStudents;
    
    private String gradeLevel;
    private BigDecimal gpaGrade;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}