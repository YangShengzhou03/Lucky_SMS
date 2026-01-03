package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Objects;

/**
 * 状态类型实体类
 */
@TableName("status_types")
public class StatusType extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer statusId;
    
    @TableField("status_code")
    private String statusCode;
    
    @TableField("status_name")
    private String statusName;
    
    @TableField("status_type")
    private String statusType;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StatusType that = (StatusType) o;
        return Objects.equals(statusId, that.statusId) &&
                Objects.equals(statusCode, that.statusCode) &&
                Objects.equals(statusName, that.statusName) &&
                Objects.equals(statusType, that.statusType) &&
                Objects.equals(description, that.description) &&
                Objects.equals(isActive, that.isActive) &&
                Objects.equals(sortOrder, that.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statusId, statusCode, statusName, statusType, description, isActive, sortOrder);
    }
}