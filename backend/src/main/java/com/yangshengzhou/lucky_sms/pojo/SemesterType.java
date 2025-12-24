package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学期类型实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}