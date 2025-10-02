package com.yangshengzhou.lucky_sms.vo;

// 封装注册成功给前端的数据类型
public class RegisterVO {
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

    // uid的getter和setter
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    // username的getter和setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // phone的getter和setter
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // role的getter和setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // token的getter和setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
