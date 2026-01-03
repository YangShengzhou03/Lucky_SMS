package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;

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
    

    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private Major major;
    
    @TableField(exist = false)
    private CourseType courseType;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(Integer courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public Integer getTheoryHours() {
        return theoryHours;
    }

    public void setTheoryHours(Integer theoryHours) {
        this.theoryHours = theoryHours;
    }

    public Integer getPracticeHours() {
        return practiceHours;
    }

    public void setPracticeHours(Integer practiceHours) {
        this.practiceHours = practiceHours;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getTextbook() {
        return textbook;
    }

    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public Boolean getIsCompulsory() {
        return isCompulsory;
    }

    public void setIsCompulsory(Boolean isCompulsory) {
        this.isCompulsory = isCompulsory;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
    }

    @Override
    public int hashCode() {
        return courseId != null ? courseId.hashCode() : 0;
    }
}