package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    

    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private Major major;
    
    @TableField(exist = false)
    private Class classInfo;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
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

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(LocalDate graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Integer getEducationYears() {
        return educationYears;
    }

    public void setEducationYears(Integer educationYears) {
        this.educationYears = educationYears;
    }

    public Integer getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(Integer educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public BigDecimal getEntranceScore() {
        return entranceScore;
    }

    public void setEntranceScore(BigDecimal entranceScore) {
        this.entranceScore = entranceScore;
    }

    public String getScholarshipInfo() {
        return scholarshipInfo;
    }

    public void setScholarshipInfo(String scholarshipInfo) {
        this.scholarshipInfo = scholarshipInfo;
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

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Class getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(Class classInfo) {
        this.classInfo = classInfo;
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
        Student student = (Student) o;
        return studentId != null ? studentId.equals(student.studentId) : student.studentId == null;
    }

    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}