package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教师职称实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}