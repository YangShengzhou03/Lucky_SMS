package com.yangshengzhou.lucky_sms.vo;

import lombok.Data;

// VO是返回给前端的数据封装类型
// 登录成功返回给前端的
@Data
public class LoginVO {
    private Integer uid;         // 用户ID
    private String username;     // 用户名
    private String phone;        // 手机号
    private String role;         // 角色
    private String token;        // 认证令牌（后续接口需要携带）
    
    // 添加与数据库字段对应的属性，用于MyBatis映射
    public void setUserId(Integer userId) {
        this.uid = userId;
    }
    
    public void setRoleName(String roleName) {
        this.role = roleName;
    }
}
