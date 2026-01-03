package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;

@TableName("majors")
public class Major extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer majorId;
    
    @TableField("major_code")
    private String majorCode;
    
    @TableField("major_name")
    private String majorName;
    
    @TableField("department_id")
    private Integer departmentId;
    
    @TableField("major_type_id")
    private Integer majorTypeId;
    
    @TableField("degree_type_id")
    private Integer degreeTypeId;
    
    @TableField("duration_years")
    private Integer durationYears;
    
    private String description;
    
    @TableField("status_id")
    private Integer statusId;
    

    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getMajorTypeId() {
        return majorTypeId;
    }

    public void setMajorTypeId(Integer majorTypeId) {
        this.majorTypeId = majorTypeId;
    }

    public Integer getDegreeTypeId() {
        return degreeTypeId;
    }

    public void setDegreeTypeId(Integer degreeTypeId) {
        this.degreeTypeId = degreeTypeId;
    }

    public Integer getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}