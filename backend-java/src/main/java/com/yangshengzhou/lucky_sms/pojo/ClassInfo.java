package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 班级实体类
 */
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

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Integer gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getHeadTeacherUserId() {
        return headTeacherUserId;
    }

    public void setHeadTeacherUserId(Integer headTeacherUserId) {
        this.headTeacherUserId = headTeacherUserId;
    }

    public Integer getCounselorUserId() {
        return counselorUserId;
    }

    public void setCounselorUserId(Integer counselorUserId) {
        this.counselorUserId = counselorUserId;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public User getHeadTeacher() {
        return headTeacher;
    }

    public void setHeadTeacher(User headTeacher) {
        this.headTeacher = headTeacher;
    }

    public User getCounselor() {
        return counselor;
    }

    public void setCounselor(User counselor) {
        this.counselor = counselor;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}