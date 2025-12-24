package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    
    @TableField("real_name")
    private String realName;
    
    private String gender;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    private String address;
    
    @TableField("user_type")
    private String userType;
    
    @TableField("status_id")
    private Integer statusId;

    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private String token;
    
    // 关联查询字段
    @TableField(exist = false)
    private StatusType status;
}