package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 考核方式实体类
 */
@TableName("assessment_methods")
public class AssessmentMethod extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer assessmentMethodId;
    
    @TableField("method_code")
    private String methodCode;
    
    @TableField("method_name")
    private String methodName;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;

    public Integer getAssessmentMethodId() {
        return assessmentMethodId;
    }

    public void setAssessmentMethodId(Integer assessmentMethodId) {
        this.assessmentMethodId = assessmentMethodId;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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