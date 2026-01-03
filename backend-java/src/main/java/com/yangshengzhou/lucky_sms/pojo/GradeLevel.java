package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;

/**
 * 成绩等级实体类
 */
@TableName("grade_levels")
public class GradeLevel extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer gradeLevelId;
    
    @TableField("level_code")
    private String levelCode;
    
    @TableField("level_name")
    private String levelName;
    
    @TableField("min_score")
    private BigDecimal minScore;
    
    @TableField("max_score")
    private BigDecimal maxScore;
    
    @TableField("gpa_points")
    private BigDecimal gpaPoints;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;

    public Integer getGradeLevelId() {
        return gradeLevelId;
    }

    public void setGradeLevelId(Integer gradeLevelId) {
        this.gradeLevelId = gradeLevelId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getMinScore() {
        return minScore;
    }

    public void setMinScore(BigDecimal minScore) {
        this.minScore = minScore;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    public BigDecimal getGpaPoints() {
        return gpaPoints;
    }

    public void setGpaPoints(BigDecimal gpaPoints) {
        this.gpaPoints = gpaPoints;
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