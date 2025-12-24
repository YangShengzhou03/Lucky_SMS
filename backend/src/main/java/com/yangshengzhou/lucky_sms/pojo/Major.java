package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 专业实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("majors")
public class Major extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer majorId;
    
    @TableField("major_code")
    private String majorCode;
    
    @TableField("major_name")
    private String majorName;
    
    @TableField("department_id")
    private Integer departmentId;
    
    @TableField("major_type_id")
    private Integer majorTypeId;
    
    @TableField("degree_type_id")
    private Integer degreeTypeId;
    
    @TableField("duration_years")
    private Integer durationYears;
    
    private String description;
    
    @TableField("status_id")
    private Integer statusId;
    

    @TableField(exist = false)
    private Department department;
    
    @TableField(exist = false)
    private StatusType status;
}