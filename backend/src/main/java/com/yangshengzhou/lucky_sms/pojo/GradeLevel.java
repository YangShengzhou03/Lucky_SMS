package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 成绩等级实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}