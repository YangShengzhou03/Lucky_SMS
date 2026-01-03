package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResearchDirection() {
        return researchDirection;
    }

    public void setResearchDirection(String researchDirection) {
        this.researchDirection = researchDirection;
    }

    public String getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
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
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(teacherId, teacher.teacherId) &&
                Objects.equals(teacherNo, teacher.teacherNo) &&
                Objects.equals(userId, teacher.userId) &&
                Objects.equals(departmentId, teacher.departmentId) &&
                Objects.equals(titleId, teacher.titleId) &&
                Objects.equals(hireDate, teacher.hireDate) &&
                Objects.equals(officeLocation, teacher.officeLocation) &&
                Objects.equals(phone, teacher.phone) &&
                Objects.equals(email, teacher.email) &&
                Objects.equals(researchDirection, teacher.researchDirection) &&
                Objects.equals(educationBackground, teacher.educationBackground) &&
                Objects.equals(workExperience, teacher.workExperience) &&
                Objects.equals(statusId, teacher.statusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teacherId, teacherNo, userId, departmentId, titleId, hireDate, officeLocation, phone, email, researchDirection, educationBackground, workExperience, statusId);
    }
}