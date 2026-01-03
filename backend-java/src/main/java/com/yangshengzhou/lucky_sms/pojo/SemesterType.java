package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

@TableName("semester_types")
public class SemesterType extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer semesterTypeId;
    
    @TableField("type_code")
    private String typeCode;
    
    @TableField("type_name")
    private String typeName;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;

    public Integer getSemesterTypeId() {
        return semesterTypeId;
    }

    public void setSemesterTypeId(Integer semesterTypeId) {
        this.semesterTypeId = semesterTypeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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