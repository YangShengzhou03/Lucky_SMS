package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("course_grades")
public class CourseGrade extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer gradeId;
    
    @TableField("student_id")
    private Integer studentId;
    
    @TableField("assignment_id")
    private Integer assignmentId;
    
    @TableField("usual_score")
    private BigDecimal usualScore;
    
    @TableField("midterm_score")
    private BigDecimal midtermScore;
    
    @TableField("final_score")
    private BigDecimal finalScore;
    
    @TableField("total_score")
    private BigDecimal totalScore;
    
    @TableField("gpa_grade")
    private BigDecimal gpaGrade;
    
    @TableField("grade_level_id")
    private Integer gradeLevelId;
    
    @TableField("make_up_score")
    private BigDecimal makeUpScore;
    
    @TableField("retake_score")
    private BigDecimal retakeScore;
    
    @TableField("review_status_id")
    private Integer reviewStatusId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("review_time")
    private LocalDateTime reviewTime;
    
    @TableField("reviewer_id")
    private Integer reviewerId;
    
    @TableField("review_comment")
    private String reviewComment;
    
    @TableField("status_id")
    private Integer statusId;
    

    @TableField(exist = false)
    private Student student;
    
    @TableField(exist = false)
    private TeachingAssignment assignment;
    
    @TableField(exist = false)
    private User reviewer;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
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

    public BigDecimal getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(BigDecimal usualScore) {
        this.usualScore = usualScore;
    }

    public BigDecimal getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(BigDecimal midtermScore) {
        this.midtermScore = midtermScore;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getGpaGrade() {
        return gpaGrade;
    }

    public void setGpaGrade(BigDecimal gpaGrade) {
        this.gpaGrade = gpaGrade;
    }

    public Integer getGradeLevelId() {
        return gradeLevelId;
    }

    public void setGradeLevelId(Integer gradeLevelId) {
        this.gradeLevelId = gradeLevelId;
    }

    public BigDecimal getMakeUpScore() {
        return makeUpScore;
    }

    public void setMakeUpScore(BigDecimal makeUpScore) {
        this.makeUpScore = makeUpScore;
    }

    public BigDecimal getRetakeScore() {
        return retakeScore;
    }

    public void setRetakeScore(BigDecimal retakeScore) {
        this.retakeScore = retakeScore;
    }

    public Integer getReviewStatusId() {
        return reviewStatusId;
    }

    public void setReviewStatusId(Integer reviewStatusId) {
        this.reviewStatusId = reviewStatusId;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}