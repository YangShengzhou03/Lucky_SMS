package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@TableName("departments")
public class Department extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer departmentId;
    
    @TableField("department_code")
    private String departmentCode;
    
    @TableField("department_name")
    private String departmentName;
    
    @TableField("department_type_id")
    private Integer departmentTypeId;
    
    @TableField("parent_department_id")
    private Integer parentDepartmentId;
    
    private String description;
    private String phone;
    private String email;
    private String address;
    private String website;
    
    @TableField("dean_user_id")
    private Integer deanUserId;
    
    @TableField("status_id")
    private Integer statusId;
    
    // 关联查询字段
    @TableField(exist = false)
    private User dean;
    
    @TableField(exist = false)
    private Department parentDepartment;
    
    @TableField(exist = false)
    private List<Department> subDepartments;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentTypeId() {
        return departmentTypeId;
    }

    public void setDepartmentTypeId(Integer departmentTypeId) {
        this.departmentTypeId = departmentTypeId;
    }

    public Integer getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(Integer parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getDeanUserId() {
        return deanUserId;
    }

    public void setDeanUserId(Integer deanUserId) {
        this.deanUserId = deanUserId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public User getDean() {
        return dean;
    }

    public void setDean(User dean) {
        this.dean = dean;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public List<Department> getSubDepartments() {
        return subDepartments;
    }

    public void setSubDepartments(List<Department> subDepartments) {
        this.subDepartments = subDepartments;
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
        Department that = (Department) o;
        return Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(departmentCode, that.departmentCode) &&
                Objects.equals(departmentName, that.departmentName) &&
                Objects.equals(departmentTypeId, that.departmentTypeId) &&
                Objects.equals(parentDepartmentId, that.parentDepartmentId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(website, that.website) &&
                Objects.equals(deanUserId, that.deanUserId) &&
                Objects.equals(statusId, that.statusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), departmentId, departmentCode, departmentName, departmentTypeId, parentDepartmentId, description, phone, email, address, website, deanUserId, statusId);
    }
}