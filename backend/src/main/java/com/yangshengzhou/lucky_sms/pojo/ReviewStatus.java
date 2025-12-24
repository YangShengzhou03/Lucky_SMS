package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审核状态实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}