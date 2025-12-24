package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程类型实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_types")
public class CourseType extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer courseTypeId;
    
    @TableField("type_code")
    private String typeCode;
    
    @TableField("type_name")
    private String typeName;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;
}