package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getSemesterTypeId() {
        return semesterTypeId;
    }

    public void setSemesterTypeId(Integer semesterTypeId) {
        this.semesterTypeId = semesterTypeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCourseSelectionStart() {
        return courseSelectionStart;
    }

    public void setCourseSelectionStart(LocalDateTime courseSelectionStart) {
        this.courseSelectionStart = courseSelectionStart;
    }

    public LocalDateTime getCourseSelectionEnd() {
        return courseSelectionEnd;
    }

    public void setCourseSelectionEnd(LocalDateTime courseSelectionEnd) {
        this.courseSelectionEnd = courseSelectionEnd;
    }

    public LocalDateTime getGradeEntryStart() {
        return gradeEntryStart;
    }

    public void setGradeEntryStart(LocalDateTime gradeEntryStart) {
        this.gradeEntryStart = gradeEntryStart;
    }

    public LocalDateTime getGradeEntryEnd() {
        return gradeEntryEnd;
    }

    public void setGradeEntryEnd(LocalDateTime gradeEntryEnd) {
        this.gradeEntryEnd = gradeEntryEnd;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public SemesterType getSemesterType() {
        return semesterType;
    }

    public void setSemesterType(SemesterType semesterType) {
        this.semesterType = semesterType;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}