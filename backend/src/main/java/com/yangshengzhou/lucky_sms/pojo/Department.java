package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学院实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("departments")
public class Department extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer departmentId;
    
    @TableField("department_code")
    private String departmentCode;
    
    @TableField("department_name")
    private String departmentName;
    
    @TableField("department_type_id")
    private Integer departmentTypeId;
    
    @TableField("parent_department_id")
    private Integer parentDepartmentId;
    
    private String description;
    private String phone;
    private String email;
    private String address;
    private String website;
    
    @TableField("dean_user_id")
    private Integer deanUserId;
    
    @TableField("status_id")
    private Integer statusId;
    
    // 关联查询字段
    @TableField(exist = false)
    private User dean;
    
    @TableField(exist = false)
    private Department parentDepartment;
    
    @TableField(exist = false)
    private List<Department> subDepartments;
    
    @TableField(exist = false)
    private StatusType status;
}