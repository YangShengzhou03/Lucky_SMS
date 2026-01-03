package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Objects;

@TableName("teacher_titles")
public class Title extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer titleId;
    
    @TableField("title_code")
    private String titleCode;
    
    @TableField("title_name")
    private String titleName;
    
    @TableField("title_level")
    private Integer titleLevel;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Integer getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(Integer titleLevel) {
        this.titleLevel = titleLevel;
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
        Title title = (Title) o;
        return Objects.equals(titleId, title.titleId) &&
                Objects.equals(titleCode, title.titleCode) &&
                Objects.equals(titleName, title.titleName) &&
                Objects.equals(titleLevel, title.titleLevel) &&
                Objects.equals(description, title.description) &&
                Objects.equals(isActive, title.isActive) &&
                Objects.equals(sortOrder, title.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), titleId, titleCode, titleName, titleLevel, description, isActive, sortOrder);
    }
}