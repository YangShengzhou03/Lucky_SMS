package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 审核状态实体类
 */
@TableName("review_statuses")
public class ReviewStatus extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer reviewStatusId;
    
    @TableField("status_code")
    private String statusCode;
    
    @TableField("status_name")
    private String statusName;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;

    public Integer getReviewStatusId() {
        return reviewStatusId;
    }

    public void setReviewStatusId(Integer reviewStatusId) {
        this.reviewStatusId = reviewStatusId;
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
}