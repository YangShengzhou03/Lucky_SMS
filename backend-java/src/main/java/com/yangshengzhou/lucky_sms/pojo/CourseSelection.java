package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    

    @TableField(exist = false)
    private Student student;
    
    @TableField(exist = false)
    private TeachingAssignment assignment;
    
    @TableField(exist = false)
    private SelectionType selectionType;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(Integer selectionId) {
        this.selectionId = selectionId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public LocalDateTime getSelectionTime() {
        return selectionTime;
    }

    public void setSelectionTime(LocalDateTime selectionTime) {
        this.selectionTime = selectionTime;
    }

    public Integer getSelectionTypeId() {
        return selectionTypeId;
    }

    public void setSelectionTypeId(Integer selectionTypeId) {
        this.selectionTypeId = selectionTypeId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getDropTime() {
        return dropTime;
    }

    public void setDropTime(LocalDateTime dropTime) {
        this.dropTime = dropTime;
    }

    public String getDropReason() {
        return dropReason;
    }

    public void setDropReason(String dropReason) {
        this.dropReason = dropReason;
    }

    public BigDecimal getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(BigDecimal finalGrade) {
        this.finalGrade = finalGrade;
    }

    public BigDecimal getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(BigDecimal attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public BigDecimal getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(BigDecimal evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public TeachingAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(TeachingAssignment assignment) {
        this.assignment = assignment;
    }

    public SelectionType getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionType selectionType) {
        this.selectionType = selectionType;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}