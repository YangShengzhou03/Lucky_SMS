package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考核方式实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}