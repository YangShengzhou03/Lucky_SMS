package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 状态类型实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("status_types")
public class StatusType extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer statusId;
    
    @TableField("status_code")
    private String statusCode;
    
    @TableField("status_name")
    private String statusName;
    
    @TableField("status_type")
    private String statusType;
    
    private String description;
    
    @TableField("is_active")
    private Boolean isActive;
    
    private Integer sortOrder;
}